package com.avacado.stupidapps.joana.domain.notifiers;

import java.util.List;


public interface JoanaNotifier {

    void notify(List<String> emailIds, String message);
}
