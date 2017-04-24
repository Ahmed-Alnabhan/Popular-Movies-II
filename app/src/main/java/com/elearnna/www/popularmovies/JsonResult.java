package com.elearnna.www.popularmovies;

import org.json.JSONObject;

/**
 * Created by Ahmed on 4/18/2017.
 */

public interface JsonResult {
    void onFinishJsonReading(JSONObject[] s);
}
