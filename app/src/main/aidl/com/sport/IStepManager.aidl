// IStepManager.aidl
package com.sport;

// Declare any non-default types here with import statements
import com.sport.IStepListener;

interface IStepManager {

    void registerStepListener(IStepListener listener);
    void unregisterStepListener(IStepListener listener);
    void notifyStep();

}
