package org.gamesys;

public class DummyExclusionServiceImpl implements ExclusionService {
    @Override
    public boolean validate(String dob, String ssn) {
        return false;
    }
}
