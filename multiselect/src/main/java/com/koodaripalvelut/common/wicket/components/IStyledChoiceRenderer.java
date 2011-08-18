/**
 * Copyright (c) Koodaripalvelut.com Finland 2008
 */
package com.koodaripalvelut.common.wicket.components;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * @author Martin
 *
 * Copyright (c) Koodaripalvelut.com Finland 2008
 * 
 * @param <T> 
 */
public interface IStyledChoiceRenderer<T> extends IChoiceRenderer<T> {
  /**
   * @param t 
   * @return String
   */
  public String getOptGroupLabel(T t);

  /**
   * @param t 
   * @return String
   */
  public String getOptionCssClassName(T t);
}
