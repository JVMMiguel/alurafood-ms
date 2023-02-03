package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;

public class AwsInfraApp {
    public static void main(final String[] args) {
        App app = new App();
        new AwsInfraVpcStack(app, "Vpc");

        app.synth();
    }
}

