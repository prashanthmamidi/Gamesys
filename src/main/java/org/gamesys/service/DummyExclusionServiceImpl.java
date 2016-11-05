package org.gamesys.service;

import org.gamesys.service.ExclusionService;

public class DummyExclusionServiceImpl implements ExclusionService {
    @Override
    public boolean validate(String dob, String ssn) {
        return !(dob.equals("1996/11/05") && ssn.equals("123-45-5890"));
    }
}
