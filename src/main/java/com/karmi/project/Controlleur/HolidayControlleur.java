package com.karmi.project.Controlleur;


import com.karmi.project.Interface.IHolidayService;
import com.karmi.project.entitie.Holiday;
import com.karmi.project.entitie.MedicalStaff;
import com.karmi.project.repositories.HolidayRepository;
import com.karmi.project.repositories.MedicalStaffRepository;
import lombok.AllArgsConstructor;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;

@RestController
@AllArgsConstructor
@RequestMapping("/holiday")

public class HolidayControlleur {
    
@Autowired
    IHolidayService HolidayService;
@Autowired
    HolidayRepository holidayRepository;
@Autowired
MedicalStaffRepository medicalStaffRepository;

    @PostMapping("/add-holiday")
    public Holiday addholiday(@RequestBody Holiday holiday) {

       return HolidayService.ajouterHoliday(holiday);
    };


    @PostMapping("/CreateHoldiay/{id}")
    public Holiday CreateHoldiay( @RequestBody Holiday holiday, @PathVariable("id") Long id) throws MessagingException, GeneralSecurityException, IOException {
        return  HolidayService.createEvent(holiday,id);

    }
    @Scheduled(cron = "0 * * ? * *")

    @GetMapping("/get")
    public void cronMethod() {
        LocalDate curDate = LocalDate.now();
        List<Holiday> holidays = holidayRepository.findAll();
        List<Long> expirationSoonList = new ArrayList<>();
        for (Holiday holiday : holidays) {
            LocalDate   endDate = (holiday.getEndDate());
            Period  period=Period.between(curDate,endDate);

            int diffInDays =period.getDays();

            if (diffInDays ==7) {
                expirationSoonList.add(holiday.getIdHoliday());
            }

            System.out.println("list of Holliday:" + expirationSoonList);
        }
    }

    @PutMapping("/update")

    public Holiday updateHoliday(@RequestBody Holiday holiday) {
       return HolidayService.updateHoliday(holiday);
    }


    @DeleteMapping("/deleteHoliday/{idHoliday}")
    public String removeHoliday(@PathVariable("idHoliday") Long idHoliday){
        HolidayService.removeHoliday(idHoliday);
        return "delete success";
    }
    @GetMapping("/getAllHoliday")
    public List<Holiday> retrieveAllHolidays() {
        return HolidayService.retrieveAllHoliday();
    }

    @GetMapping("/getAllHoliday/{idStaff}")
    public  List<Holiday> findHolidayByMedicalStaffIdStaffAndStartDateAndAndEndDate(@PathVariable ("idStaff") Long idStaff, @RequestParam("start")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        return holidayRepository.findHolidayByMedicalStaffIdStaffAndStartDateAndAndEndDate(idStaff, startDate , endDate);
    }
    @GetMapping("/type/{tt}")
    public List<Holiday> findBytype( @PathVariable("tt") String type){
        return  holidayRepository.findByType(type);
    }





    @GetMapping("/calendarrr")
    public ResponseEntity<String> getHolidayCalendar(@RequestParam("email") String email) {
        // Récupération du personnel médical associé à l'e-mail fourni
        MedicalStaff staff = medicalStaffRepository.findByEmail(email);
        if (staff == null) {
            return ResponseEntity.badRequest().body("Aucun personnel médical trouvé avec cet e-mail.");
        }

        // Récupération de tous les congés du personnel médical associé
        List<Holiday> holidays = holidayRepository.findByMedicalStaff(staff);

        // Création de la chaîne HTML pour le calendrier
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>Calendrier des congés</title>");
        sb.append("<style>");
        sb.append("table {");
        sb.append("font-family: Arial, sans-serif;");
        sb.append("border-collapse: collapse;");
        sb.append("width: 100%;");
        sb.append("}");
        sb.append("th, td {");
        sb.append("border: 1px solid #ddd;");
        sb.append("padding: 8px;");
        sb.append("text-align: center;");
        sb.append("}");
        sb.append("th {");
        sb.append("background-color: #4CAF50;");
        sb.append("color: white;");
        sb.append("}");
        sb.append("td.valid {");
        sb.append("background-color: #e6ffe6;");
        sb.append("}");
        sb.append("td.invalid {");
        sb.append("background-color: #ffe6e6;");
        sb.append("}");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<h1>Calendrier des congés</h1>");
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<th>Date</th>");
        sb.append("<th>Statut</th>");
        sb.append("</tr>");

        // Création d'une Map avec les congés par date pour faciliter l'affichage dans le tableau
        Map<LocalDate, List<Holiday>> holidaysByDate = holidays.stream()
                .collect(Collectors.groupingBy(Holiday::getStartDate));

        // Affichage de chaque date du mois en cours
        LocalDate date = LocalDate.now();
        while (date.getMonth() == LocalDate.now().getMonth()) {
            sb.append("<tr>");
            sb.append("<td>").append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>");

            // Affichage du statut pour la date en cours
            List<Holiday> holidaysForDate = holidaysByDate.getOrDefault(date, Collections.emptyList());
            if (holidaysForDate.isEmpty()) {
                sb.append("<td>Pas de congé</td>");
            } else {
                boolean isValid = holidaysForDate.stream().allMatch(Holiday::getIsValid);
                if (isValid) {
                    sb.append("<td class=\"valid\">Tous les congés sont valides</td>");
                } else {
                    sb.append("<td class=\"invalid\">Au moins un congé n'est pas valide</td>");
                }
            }

            sb.append("</tr>");
            date = date.plusDays(1);
        }

        sb.append("</table>");
        sb.append("</body>");
        sb.append("</html>");

        return ResponseEntity.ok(sb.toString());
    }




    @GetMapping("/calendarr")
    public ResponseEntity<String> getHolidayCalendarr(@RequestParam("email") String email) {
        // Récupération du personnel médical associé à l'e-mail fourni
        MedicalStaff staff = medicalStaffRepository.findByEmail(email);
        if (staff == null) {
            return ResponseEntity.badRequest().body("Aucun personnel médical trouvé avec cet e-mail.");
        }

        // Récupération de tous les congés du personnel médical associé
        List<Holiday> holidays = holidayRepository.findByMedicalStaff(staff);

        // Création de la chaîne HTML pour le calendrier
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<style>");
        sb.append(".vacation { background-color: #900C3F ; }");
        sb.append(".sick { background-color: #C70039; }");
        sb.append(".other { background-color: #581845; }");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<h1>Calendrier des congés</h1>");
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<th>Début</th>");
        sb.append("<th>Fin</th>");
        sb.append("<th>Type</th>");
        sb.append("<th>Valide</th>");
        sb.append("</tr>");
        for (Holiday holiday : holidays) {
            sb.append("<tr class='");
            switch(holiday.getType()) {
                case "Vacation":
                    sb.append("vacation'>");
                    sb.append("<td><i class='far fa-sun'></i> ");
                    break;
                case "Sick":
                    sb.append("sick'>");
                    sb.append("<td><i class='fas fa-thermometer-half'></i> ");
                    break;
                default:
                    sb.append("other'>");
                    sb.append("<td><i class='fas fa-calendar-alt'></i> ");
                    break;
            }
            sb.append(holiday.getStartDate().toString()).append("</td>");
            sb.append("<td>").append(holiday.getEndDate().toString()).append("</td>");
            sb.append("<td>").append(holiday.getType()).append("</td>");
            sb.append("<td>").append(holiday.getIsValid() ? "Oui" : "Non").append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("<div class='legend'>");
        sb.append("<h2>Légende</h2>");
        sb.append("<ul>");
        sb.append("<li><span class='vacation'></span> Congé</li>");
        sb.append("<li><span class='sick'></span> Maladie</li>");
        sb.append("<li><span class='other'></span> Autre</li>");
        sb.append("</ul>");
        sb.append("</div>");
        sb.append("</body>");
        sb.append("</html>");

        return ResponseEntity.ok(sb.toString());
    }

}







