package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;

public class AwsInfraApp {
    public static void main(final String[] args) {
        App app = new App();

        AwsInfraVpcStack vpcStack = new AwsInfraVpcStack(app, "Vpc");
        AwsInfraClusterStack clusterStack = new AwsInfraClusterStack(app, "Cluster", vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        app.synth();
    }
}

