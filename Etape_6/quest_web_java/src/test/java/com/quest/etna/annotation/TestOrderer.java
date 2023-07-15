package com.quest.etna.annotation;

import org.junit.jupiter.api.ClassDescriptor;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;

import java.util.Comparator;

public class TestOrderer implements ClassOrderer {
    @Override
    public void orderClasses(ClassOrdererContext classOrdererContext) {
        classOrdererContext.getClassDescriptors().sort(Comparator.comparingInt(TestOrderer::getOrder));
    }

    private static int getOrder(ClassDescriptor classDescriptor) {
        if (classDescriptor.findAnnotation(Authentication.class).isPresent()) {
            return 1;
        } else if (classDescriptor.findAnnotation(User.class).isPresent()) {
            return 2;
        } else if (classDescriptor.findAnnotation(Address.class).isPresent()) {
            return 3;
        } else if (classDescriptor.findAnnotation(Reservation.class).isPresent()){
            return 4;
        }else if (classDescriptor.findAnnotation(Review.class).isPresent()){
            return 5;
        }
        else {
            return 6;
        }
    }
}