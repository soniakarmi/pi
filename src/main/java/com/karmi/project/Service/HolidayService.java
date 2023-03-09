package com.karmi.project.Service;


import com.google.api.client.auth.oauth2.Credential;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.karmi.project.Interface.IEmailServive;
import com.karmi.project.Interface.IHolidayService;
import com.karmi.project.entitie.Holiday;
import com.karmi.project.entitie.MedicalStaff;
import com.karmi.project.repositories.HolidayRepository;
import com.karmi.project.repositories.MedicalStaffRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
public class HolidayService implements IHolidayService {
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String APPLICATION_NAME = "Service Account";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "client_secret.json";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    @Autowired
    HolidayRepository holidayRepository;

    @Autowired
    MedicalStaffRepository medicalStaffRepository;





@Autowired
EmailService emailService;
    @Override
    public Holiday ajouterHoliday(Holiday holiday) {
        return holidayRepository.save(holiday);

    }
    @Override
    public Holiday updateHoliday(Holiday holiday) {
        return holidayRepository.save(holiday);

    }

    @Override
    public List<Holiday> retrieveAllHoliday() {
        return  holidayRepository.findAll();
    }

    @Override
    public void removeHoliday(Long idHoliday) {
        holidayRepository.deleteById(idHoliday);
    }

    @Override
    public Holiday createEvent(Holiday holiday,Long id) throws MessagingException, IOException, GeneralSecurityException {
       // List<MedicalStaff> userList = new ArrayList<>();
        MedicalStaff medicalStaff=medicalStaffRepository.findById(id).get();
       // MedicalStaff medicalStaff= holidayRepository.findHolidayByMedicalStaffIdStaff(holiday.getMedicalStaff().getIdStaff()).getMedicalStaff();
        this.newEvent(medicalStaff,holiday);
        this.emailService.sendMailWithAttachment(holiday, medicalStaff,"hhhhh");
         return this.holidayRepository.save(holiday);

    }
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream inputStream = new ClassPathResource("credentials.json").getInputStream();
        InputStream inputStreamTokens = new ClassPathResource(TOKENS_DIRECTORY_PATH).getInputStream();

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStreamTokens));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(String.valueOf(inputStream))))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp
        (flow, receiver).authorize("user");
    }
    // Méthode pour récupérer un calendrier de congé à partir des dates de début et de fin
    private List<LocalDate> getHolidayCalendar(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> calendar = new ArrayList<>();
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            calendar.add(date);
            date = date.plusDays(1);
        }
        return calendar;
    }

    public List<String> newEvent(MedicalStaff attendeeEmails, Holiday event) throws IOException, GeneralSecurityException {

        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();


        Credential credential = getCredentials(httpTransport);
        Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Create a new event with conference data
        List<EventAttendee> attendees=new ArrayList<>();
        com.google.api.services.calendar.model.Event.Organizer organizer =new com.google.api.services.calendar.model.Event.Organizer();
        com.google.api.services.calendar.model.Event event2 = new com.google.api.services.calendar.model.Event();
        organizer.setEmail("sonia.karmi@esprit.tn");
        event2.setOrganizer(organizer);
        event2.setSummary(event.getType());
       // event2.setDescription(event.getDescription());

        DateTime startDateTime = new DateTime(String.valueOf(event.getStartDate()));
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Europe/Paris");
        event2.setStart(start);

        DateTime endDateTime = new DateTime(String.valueOf(event.getEndDate()));
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Europe/Paris");
        event2.setEnd(end);



            EventAttendee attendee = new EventAttendee();
            attendee.setEmail(attendeeEmails.getEmail());
            attendees.add(attendee);


        event2.setAttendees(attendees);
        ConferenceSolutionKey conferenceSolutionKey = new ConferenceSolutionKey()
                .setType("hangoutsMeet");
        CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest()
                .setRequestId(UUID.randomUUID().toString())
                .setConferenceSolutionKey(conferenceSolutionKey);
        ConferenceData conferenceData = new ConferenceData()
                .setCreateRequest(createConferenceRequest);
        EventReminder c =new EventReminder();
        c.setMinutes(10);

        event2.setConferenceData(conferenceData);
        com.google.api.services.calendar.model.Event createdEvent = service.events().insert("primary", event2).setConferenceDataVersion(1).setSendNotifications(true).execute();
// Print the Google Meet link

        String meetLink = createdEvent.getHangoutLink();
        String htmlLink = createdEvent.getHtmlLink();

        System.out.printf("Google Meet link: %s\n", meetLink);
        System.out.println(createdEvent);

        List<String> ls = new ArrayList<>();
        ls.add(meetLink);
        ls.add(htmlLink);
        // handle the exception
        System.out.println("TEST");
        return ls;
    }
    public Holiday getHolidayById(Long id) {
        return holidayRepository.findById(id).orElse(null);
    }








}
