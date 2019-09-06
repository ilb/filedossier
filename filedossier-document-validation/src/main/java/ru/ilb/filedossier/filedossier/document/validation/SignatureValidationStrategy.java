/*
 * Copyright 2019 kuznetsov_me.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.ilb.filedossier.filedossier.document.validation;

import java.util.List;

/**
 *
 * @author kuznetsov_me
 */
public class SignatureValidationStrategy implements ValidationStrategy {

    private static final String SIGN_NOT_EXIST_ERR = "Signature doesn't exist: ";

    private ValidationReport report;

    private SignatureDetector signatureDetector;

    public SignatureValidationStrategy(SignatureDetector signatureDetector) {
        this.signatureDetector = signatureDetector;
        report = new ValidationReport();
    }

    @Override
    public ValidationReport validate() {

        List<Boolean> signatures = signatureDetector.detectSignatures();

        int i = 1;
        for (boolean isSignatureExist : signatures) {
            if (isSignatureExist == false) report.addError(SIGN_NOT_EXIST_ERR + i++);
        }
        return report;
    }
}
