package com.CS6301Backend1.ptt.views;

public class ReportViews {

    public interface IncludeDefault {}

    public interface IncludeCompleted extends IncludeDefault{}

    public interface IncludeHours extends IncludeDefault{}

    public interface IncludeAll extends IncludeCompleted, IncludeHours {}
}
