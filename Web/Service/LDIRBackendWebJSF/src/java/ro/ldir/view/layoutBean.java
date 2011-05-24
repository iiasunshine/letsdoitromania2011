/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.ldir.view;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.ResizeEvent;
import org.primefaces.event.ToggleEvent;

public class layoutBean
{

    public void handleClose(CloseEvent event)
    {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unit Closed", "Closed unit id:'" + event.getComponent().getId() + "'");
        addMessage(message);
    }

    public void handleToggle(ToggleEvent event)
    {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " toggled", "Status:" + event.getVisibility().name());
        addMessage(message);
    }

    public void handleResize(ResizeEvent event)
    {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " resized", "Width:" + event.getWidth() + ", Height:" + event.getHeight());
        addMessage(message);
    }

    protected void addMessage(FacesMessage message)
    {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
