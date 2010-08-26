package com.koodaripalvelut.common.wicket.component.fullcalendar;

public enum Views {
  MONTH {
    @Override
    public String toString() {
      return "month";
    }
  },

  WEEK {
    @Override
    public String toString() {
      return "basicWeek";
    }
  },

  DAY {
    @Override
    public String toString() {
      return "basicDay";
    }
  },

  AGENDA_WEEK {
    @Override
    public String toString() {
      return "agendaWeek";
    }
  },

  AGENDA_DAY {
    @Override
    public String toString() {
      return "agendaDay";
    }
  };
}
