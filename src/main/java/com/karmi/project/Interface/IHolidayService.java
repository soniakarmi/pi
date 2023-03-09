package com.karmi.project.Interface;


import com.karmi.project.entitie.Holiday;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface IHolidayService {
    public Holiday ajouterHoliday (Holiday holiday);
    public Holiday updateHoliday (Holiday holiday);
    List<Holiday> retrieveAllHoliday();

    public void removeHoliday(Long idHoliday);
    Holiday createEvent(Holiday holiday,Long id) throws MessagingException, IOException, GeneralSecurityException;

    Holiday getHolidayById(Long id);
}
