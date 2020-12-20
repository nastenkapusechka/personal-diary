package com.nastenkapusechka.mypersonaldiary.entities;

import lombok.Data;

@Data
public class Compliment {
    private String compliment;

    public void setFirstLetterToUpperCase() {
        this.compliment = compliment.substring(0, 1).toUpperCase() + compliment.substring(1);
    }
}
