/*
 * Copyright (C) 2005-2010 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.web.bean.admin;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.alfresco.web.config.ConfigAdminInterpreter;


/**
 * Backing bean to support the Web Client Config Admin Console
 */
public class ConfigAdminConsoleBean
{
    // command
    private String command = "";
    private String submittedCommand = "none";
    private long duration = 0L;
    private String result = null;

    // supporting repository services
    private ConfigAdminInterpreter configAdminInterpreter;


    /**
     * @param configAdminInterpreter  Web Client config admin interpreter
     */
    public void setConfigAdminInterpreter(ConfigAdminInterpreter configAdminInterpreter)
    {
        this.configAdminInterpreter = configAdminInterpreter;
    }

    /**
     * Gets the command result
     *
     * @return  result
     */
    public String getResult()
    {
        if (result == null)
        {
            interpretCommand("help");
        }
        return result;
    }

    /**
     * Sets the command result
     *
     * @param result
     */
    public void setResult(String result)
    {
        this.result = result;
    }

    /**
     * Gets the current query
     *
     * @return  query statement
     */
    public String getCommand()
    {
        return command;
    }

    /**
     * Set the current command
     *
     * @param command   command
     */
    public void setCommand(String command)
    {
        this.command = command;
    }

    /**
     * Gets the submitted command
     *
     * @return  submitted command
     */
    public String getSubmittedCommand()
    {
        return submittedCommand;
    }

    /**
     * Set the submitted command
     *
     * @param submittedCommand The submitted command
     */
    public void setSubmittedCommand(String submittedCommand)
    {
        this.submittedCommand = submittedCommand;
    }

    /**
     * Gets the last command duration
     *
     * @return  command duration
     */
    public long getDuration()
    {
        return duration;
    }

    /**
     * Set the duration
     *
     * @param duration   The duration
     */
    public void setDuration(long duration)
    {
        this.duration = duration;
    }

    /**
     * Action to submit command
     *
     * @return  next action
     */
    public String submitCommand()
    {
        interpretCommand(command);
        return "success";
    }

    /**
     * Gets the current user name
     *
     * @return  user name
     */
    public String getCurrentUserName()
    {
        return configAdminInterpreter.getCurrentUserName();
    }

    /**
     * Interpret repo admin console command
     *
     * @param command  command
     */
    private void interpretCommand(String command)
    {
        try
        {
            long startms = System.currentTimeMillis();
            String result = configAdminInterpreter.interpretCommand(command);
            setDuration(System.currentTimeMillis() - startms);
            setResult(result);
            setCommand("");
            setSubmittedCommand(command);
        }
        catch (Exception e)
        {
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            setResult(stackTrace.toString());
        }
    }

}