package com.example.app;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

public abstract class Question implements Serializable {

    public abstract Intent prepareActivity(Context c);
}
