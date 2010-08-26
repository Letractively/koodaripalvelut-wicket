package com.koodaripalvelut.common.wicket.component.fullcalendar;

/** FullCalendar Views
 * @see http://arshaw.com/fullcalendar/docs/views/
 *
 * @author rhansen@kindleit.net
 */
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
