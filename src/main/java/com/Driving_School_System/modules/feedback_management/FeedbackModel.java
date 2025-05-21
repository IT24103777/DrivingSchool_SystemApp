package com.Driving_School_System.modules.feedback_management;

import com.Driving_School_System.common.BaseEntity;

public class FeedbackModel extends BaseEntity {
    private String feedback;
    private int numOfStars;
    private String additionalComments;
    private String relatedTo;

    public FeedbackModel() {}

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getNumOfStars() {
        return numOfStars;
    }

    public void setNumOfStars(int numOfStars) {
        this.numOfStars = numOfStars;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public String getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(String relatedTo) {
        this.relatedTo = relatedTo;
    }
}