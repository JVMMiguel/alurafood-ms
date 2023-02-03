package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.constructs.Construct;

public class AwsInfraVpcStack extends Stack {

    private Vpc vpc;

    public AwsInfraVpcStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AwsInfraVpcStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        vpc = Vpc.Builder.create(this, "AluraVpc")
                .vpcName("vpc-alura")
                .maxAzs(3)  // Default is all AZs in region
                .build();
    }

    public Vpc getVpc() {
        return vpc;
    }

}
