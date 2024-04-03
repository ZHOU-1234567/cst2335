package com.algonquin.cst2335;
/**
 * Interface for callback to be invoked when a Volley network request completes successfully.
 * Implement this interface to define custom actions upon the successful completion of the request.
 */
public interface VolleyCallBack {
    /**
     * Method to be called when the network request is successful.
     */
    void onSuccess();
}