package io.macgyver.core.web.rythm;

import io.macgyver.core.Kernel;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rythmengine.RythmEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractTemplateView;

public class RythmView extends AbstractTemplateView {

	Logger log = LoggerFactory.getLogger(RythmView.class);

    @Override
    public boolean checkResource(Locale locale) throws Exception {
    	return getRythmEngine().resourceManager().getResource(getUrl())!=null;
      
    }

    public RythmEngine getRythmEngine() {
    	return Kernel.getInstance().getApplicationContext().getBean(RythmEngine.class);
    }
    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model,
                                             HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String templatePath = this.getTemplatePath();
        log.debug("rendering: {}",templatePath);
        response.getWriter().append(getRythmEngine().render(templatePath, model));
    }

    protected String getTemplatePath() {
  
    	return getUrl();
    	
    }
}
