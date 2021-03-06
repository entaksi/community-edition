/*
 * Copyright (C) 2005-2012 Alfresco Software Limited.
 * This file is part of Alfresco
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.po.share.dashlet;

import java.util.List;

import org.alfresco.po.share.MyTasksPage;
import org.alfresco.webdrone.HtmlPage;
import org.alfresco.webdrone.RenderElement;
import org.alfresco.webdrone.RenderTime;
import org.alfresco.webdrone.WebDrone;
import org.alfresco.webdrone.exception.PageException;
import org.alfresco.webdrone.exception.PageRenderTimeException;
import org.alfresco.po.share.FactorySharePage;
import org.alfresco.po.share.ShareLink;
import org.alfresco.po.share.task.EditTaskPage;
import org.alfresco.po.share.workflow.StartWorkFlowPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

/**
 * My tasks dashlet object, holds all element of the HTML page relating to
 * share's my tasks dashlet on dashboard page.
 * 
 * @author Michael Suzuki
 * @since 1.0
 */
public class MyTasksDashlet extends AbstractDashlet implements Dashlet
{
    private static Log logger = LogFactory.getLog(MyTasksDashlet.class);

    private static final String DATA_LIST_CSS_LOCATION = "h3 > a";
    private static final String DIV_DASHLET_CONTENT_PLACEHOLDER = "div.dashlet.my-tasks";
    private static final String LIST_OF_TASKS = "h3>a[title='Edit Task']";
    private static final By COMPLETE_TASK_BUTTON = By.cssSelector("a[href*='completed']");

    /**
     * Constructor.
     */
    protected MyTasksDashlet(WebDrone drone)
    {
        super(drone, By.cssSelector(DIV_DASHLET_CONTENT_PLACEHOLDER));
    }

    @SuppressWarnings("unchecked")
    public MyTasksDashlet render()
    {
        return render(new RenderTime(maxPageLoadingTime));
    }

    @SuppressWarnings("unchecked")
    @Override
    public MyTasksDashlet render(final long time)
    {
        return render(new RenderTime(time));
    }

    /**
     * The collection of tasks displayed on my tasks dashlet.
     * 
     * @return List<ShareLink> links
     */
    public synchronized List<ShareLink> getTasks()
    {
        return getList(DATA_LIST_CSS_LOCATION);
    }

    /**
     * Selects a task that appears on my tasks dashlet by the matching name and
     * clicks on the link.
     */
    public synchronized ShareLink selectTask(final String title)
    {
        return getLink(DATA_LIST_CSS_LOCATION, title);
    }

    @SuppressWarnings("unchecked")
    public MyTasksDashlet render(RenderTime timer)
    {
        try
        {
            while (true)
            {
                timer.start();
                synchronized (this)
                {
                    try
                    {
                        this.wait(100L);
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
                if (isEmpty(DIV_DASHLET_CONTENT_PLACEHOLDER))
                {
                    // There are no results
                    break;
                }
                else if (isVisibleResults())
                {
                    // populate results
                    break;
                }
                timer.end();
            }
        }
        catch (PageRenderTimeException te)
        {
            throw new PageException(this.getClass().getName() + " failed to render in time", te);
        }
        return this;
    }

    /**
     * Renderer to ensure the task is fully loaded.
     * 
     * @param time
     * @param taskName
     * @return
     */
    public MyTasksDashlet renderTask(final long time, String taskName)
    {
        elementRender(new RenderTime(time), RenderElement.getVisibleRenderElement(By.xpath(String.format("//h3/a[text()='%s']/../../../..", taskName))));
        return this;
    }

    /**
     * This method clicks on specific task which appears on my-tasks dashlet.
     * 
     * @param task
     * @return {@link EditTaskPage}
     */
    public HtmlPage clickOnTask(String task)
    {
        if (task == null)
        {
            throw new UnsupportedOperationException("task value of link is required");
        }

        try
        {
            for (WebElement element : drone.findAndWaitForElements(By.cssSelector(LIST_OF_TASKS)))
            {
                String taskName = element.getText().toLowerCase();
                if (taskName != null && taskName.contains(task.toLowerCase()))
                {
                    element.click();
                    return FactorySharePage.resolvePage(drone);
                }
            }

        }
        catch (Exception e)
        {
            logger.error("Unable to find the List of Tasks.", e);
        }
        throw new PageException("Unable to click task: " + task);
    }

    /**
     * select StartWorkFlow... link from myTasks dashlet.
     * 
     * @return StartWorkFlowPage
     */
    public StartWorkFlowPage selectStartWorkFlow()
    {
        try
        {
            WebElement startWorkFlow = drone.findAndWait(By.linkText("Start Workflow"));
            startWorkFlow.click();
            return new StartWorkFlowPage(drone);
        }
        catch (NoSuchElementException nse)
        {
            logger.error("Not able to find the web element", nse);
        }
        catch (TimeoutException exception)
        {
            logger.error("Exceeded time to find the web element", exception);
        }

        throw new PageException("Unable to find assign workflow.");
    }

    /**
    * Mimic click on 'Complete task' button
    *
    * @return
    */
    public MyTasksPage selectComplete()
    {
        if (dashlet == null)
        {
            dashlet = drone.findAndWait(By.cssSelector(DIV_DASHLET_CONTENT_PLACEHOLDER), 100L);
        }
        dashlet.findElement(COMPLETE_TASK_BUTTON).click();
        return drone.getCurrentPage().render();
    }
}