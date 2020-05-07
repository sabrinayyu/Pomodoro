package com.CS6301Backend1.ptt.objects;

import com.CS6301Backend1.ptt.views.ReportViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.ArrayList;

@Component
public class Report {

    @JsonIgnore
    private static int id_counter = 0;

    @JsonIgnore
    private int reportId;

    @JsonView(ReportViews.IncludeCompleted.class)
    private int completedPomodoros;

    @JsonView(ReportViews.IncludeHours.class)
    private double totalHoursWorkedOnProject;

    @JsonView(ReportViews.IncludeDefault.class)
    private Collection<Sessions2> sessions;

    public Report(Collection<Sessions> sessions, String from, String to, Boolean includeCompletedPomodoros, Boolean includeTotalHoursWorkedOnProject) {
        this.setReportId(id_counter++);
        // this.setSessions(sessions);
        Collection<Sessions2> report_sessions_list = new ArrayList<>();
        OffsetDateTime report_starttime = OffsetDateTime.parse(from);
        OffsetDateTime report_endtime = OffsetDateTime.parse(to);
        int numPomodorsInSessions = 0;
        double totalTimeSpentOnPomodors = 0;

        for (Sessions s : sessions) {
            OffsetDateTime st = OffsetDateTime.parse(s.getstartTime());
            OffsetDateTime et = OffsetDateTime.parse(s.getendTime());
            if(!(report_endtime.compareTo(st)<=0 || report_starttime.compareTo(et)>=0))
            {
                Sessions2 temp_sessions2 = new Sessions2();
                temp_sessions2.setstartingTime(s.getstartTime());
                temp_sessions2.setendingTime(s.getendTime());

                long session_length_in_seconds = et.toEpochSecond() - st.toEpochSecond();
                double session_length_in_hours = session_length_in_seconds / 3600.00;
                session_length_in_hours = Math.round (session_length_in_hours * 100.0)/ 100.0;
                temp_sessions2.sethoursWorked(session_length_in_hours);

                report_sessions_list.add(temp_sessions2);
                numPomodorsInSessions += s.getcounter();
            }

            long session_length_in_seconds = et.toEpochSecond() - st.toEpochSecond();
            double session_length_in_hours = session_length_in_seconds / 3600.00;
            totalTimeSpentOnPomodors += session_length_in_hours;
        }

        this.setSessions(report_sessions_list);
        totalTimeSpentOnPomodors = Math.round (totalTimeSpentOnPomodors * 100.0)/ 100.0;
        if(includeCompletedPomodoros)
            this.setCompletedPomodoros(numPomodorsInSessions);
        if(includeTotalHoursWorkedOnProject)
            this.setTotalHoursWorkedOnProject(totalTimeSpentOnPomodors);
    }

    public Report() {
        this.reportId = id_counter++;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getCompletedPomodoros() {
        return completedPomodoros;
    }

    public void setCompletedPomodoros(int completedPomodoros) {
        this.completedPomodoros = completedPomodoros;
    }

    public double getTotalHoursWorkedOnProject() {
        return totalHoursWorkedOnProject;
    }

    public void setTotalHoursWorkedOnProject(double totalHoursWorkedOnProject) {
        this.totalHoursWorkedOnProject = totalHoursWorkedOnProject;
    }

    public Collection<Sessions2> getSessions() {
        return sessions;
    }

    public void setSessions(Collection<Sessions2> sessions) {
        this.sessions = sessions;
    }
}