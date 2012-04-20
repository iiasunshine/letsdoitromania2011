package ro.radcom.ldir.ldirbackendwebjsf2.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class JsfUtils {

    public static FacesMessage addErrorBundleMessage(String msg) {
        ResourceBundle bundle = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "msg");

        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                bundle.getString(msg),
                bundle.getString(msg));
        FacesContext.getCurrentInstance().addMessage("error", facesMsg);
        return facesMsg;
    }

    public static FacesMessage addInfoBundleMessage(String msg) {
        ResourceBundle bundle = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "msg");

        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                bundle.getString(msg),
                bundle.getString(msg));
        FacesContext.getCurrentInstance().addMessage("info", facesMsg);
        return facesMsg;
    }

    public static FacesMessage addInfoMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("info", facesMsg);
        return facesMsg;
    }

    public static FacesMessage addInfoBundleMessage(String uicomponent_id, String msg) {
        ResourceBundle bundle = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "msg");

        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                bundle.getString(msg),
                bundle.getString(msg));
        FacesContext.getCurrentInstance().addMessage(uicomponent_id, facesMsg);
        return facesMsg;
    }

    public static String getBundleMessage(String msg) {
        ResourceBundle bundle = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "msg");
        return bundle.getString(msg);
    }

    public static FacesMessage addWarnBundleMessage(String msg) {
        ResourceBundle bundle = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "msg");

        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                bundle.getString(msg),
                bundle.getString(msg));
        FacesContext.getCurrentInstance().addMessage("warn", facesMsg);
        return facesMsg;
    }

    public static FacesMessage addWarnMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext.getCurrentInstance().addMessage("warn", facesMsg);
        return facesMsg;
    }
    
    public static FacesMessage addWarnBundleMessage(String uicomponent_id, String msg) {
        ResourceBundle bundle = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "msg");

        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                bundle.getString(msg),
                bundle.getString(msg));
        FacesContext.getCurrentInstance().addMessage(uicomponent_id, facesMsg);
        return facesMsg;
    }

    public static void restoreFacesContextMessages(HashMap<String, FacesMessage> errMessages) {
        restoreFacesContextMessages(errMessages, null);
    }

    public static void restoreFacesContextMessages(HashMap<String, FacesMessage> errMessages, String except) {
        if (!errMessages.isEmpty()) {
            Iterator<Entry<String, FacesMessage>> iter = errMessages.entrySet().iterator();
            while (iter.hasNext()) {
                Entry entry = iter.next();
                if (except == null || except.length() == 0 || !except.equalsIgnoreCase(entry.getKey().toString())) {
                    FacesContext.getCurrentInstance().addMessage(entry.getKey().toString(),
                            (FacesMessage) entry.getValue());
                }
            }
        }
    }

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void ensureAddErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addErrorMessage(String uicomponent_id, String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(uicomponent_id, facesMsg);
    }

    public static void addWarnMessage(String uicomponent_id, String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext.getCurrentInstance().addMessage(uicomponent_id, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static HttpSession getHttpSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    public static HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    public static String getRemoteIp() {
    	HttpServletRequest t=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	return t.getRemoteAddr();
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtils.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static <T> List<T> arrayToList(T[] arr) {
        if (arr == null) {
            return new ArrayList<T>();
        }
        return Arrays.asList(arr);
    }

    public static <T> Set<T> arrayToSet(T[] arr) {
        if (arr == null) {
            return new HashSet<T>();
        }
        return new HashSet(Arrays.asList(arr));
    }

    public static Object[] collectionToArray(Collection<?> c) {
        if (c == null) {
            return new Object[0];
        }
        return c.toArray();
    }

    public static <T> List<T> setToList(Set<T> set) {
        return new ArrayList<T>(set);
    }

    public static String getAsConvertedString(Object object, Converter converter) {
        return converter.getAsString(FacesContext.getCurrentInstance(), null, object);
    }

    public static String getCollectionAsString(Collection<?> collection) {
        if (collection == null || collection.size() == 0) {
            return "(No Items)";
        }
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (Object item : collection) {
            if (i > 0) {
                sb.append("<br />");
            }
            sb.append(item);
            i++;
        }
        return sb.toString();
    }

    public static Object getMangedBeanInstance(String managedBeanName) throws Exception {
        FacesContext fc = FacesContext.getCurrentInstance();
        ELResolver eLResolver = fc.getApplication().getELResolver();
        ELContext eLContext = fc.getELContext();

        return eLResolver.getValue(eLContext, null, managedBeanName);
    }

    public static String getInitParameter(String param) {
        return FacesContext.getCurrentInstance().getExternalContext().getInitParameter(param);
    }

    public static Object getServletContextAttr(String param) {
        return JsfUtils.getHttpSession().getServletContext().getAttribute(param);
    }

    public static void logServletContextAttributes(Logger log4j) {
        Enumeration<String> en = JsfUtils.getHttpSession().getServletContext().getAttributeNames();
        while (en.hasMoreElements()) {
            String sss = en.nextElement();
            log4j.info("Servlet Context Attr [" + sss + "] = " + JsfUtils.getHttpSession().getServletContext().getAttribute(sss));
        }
    }

    public static String makeContextPath(String path) {
        String tmp = path.trim();
        tmp = JsfUtils.getHttpSession().getServletContext().getRealPath("/") + tmp;
        return tmp;
    }
}
