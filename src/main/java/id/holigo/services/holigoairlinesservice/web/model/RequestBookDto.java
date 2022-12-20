package id.holigo.services.holigoairlinesservice.web.model;

import id.holigo.services.holigoairlinesservice.domain.Passenger;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class RequestBookDto implements Serializable {
    private String rqid;
    private String mmid;
    private String app;
    private String action;
    private String cpname;
    private String cpmail;
    private String cptlp;
    private String org;
    private String des;
    private String flight;
    private Integer adt;
    private Integer chd;
    private Integer inf;
    private String tgl_dep;
    private String selectedIdDep;
    private String tgl_ret;
    private String selectedIdRet;
    private String acDep;
    private String acRet;
    private List<PassengerDto> passengers = new ArrayList<>();


    public Map<String, Object> build() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("rqid", getRqid());
        map.put("mmid", getMmid());
        map.put("app", getApp());
        map.put("action", getAction());
        map.put("cpname", getCpname());
        map.put("cpmail", getCpmail());
        map.put("cptlp", getCptlp());
        map.put("org", getOrg());
        map.put("des", getDes());
        map.put("flight", getFlight());
        map.put("adt", getAdt());
        map.put("chd", getChd());
        map.put("inf", getInf());
        map.put("tgl_dep", getTgl_dep());
        map.put("selectedIDdep", getSelectedIdDep());
        map.put("acDep", getAcDep());
        if (getFlight().equals("R")) {
            map.put("acRet", getAcRet());
            map.put("tgl_ret", getTgl_ret());
            map.put("selectedIDret", getSelectedIdRet());
        }
        AtomicInteger adultCounter = new AtomicInteger(1);
        AtomicInteger childCounter = new AtomicInteger(1);
        AtomicInteger infantCounter = new AtomicInteger(1);
        for (int i = 0; i < getPassengers().size(); i++) {
            PassengerDto passengerDto = getPassengers().get(i);
            List<String> names = Arrays.asList(passengerDto.getName().split(" "));
            String firstName = "";
            StringBuilder lastName = null;
            if (names.size() == 1) {
                firstName = names.get(0);
                lastName = new StringBuilder(firstName);
            }
            int ii = 0;
            for (String name : names) {
                if (ii == 0) {
                    firstName = name;
                } else {
                    if (lastName == null) {
                        lastName = new StringBuilder(name);
                    } else {
                        lastName.append(" ").append(name);
                    }

                }
                ii++;
            }
            if (firstName.length() == 1) {
                firstName = firstName + " " + lastName;
            }
            assert lastName != null;
            if (lastName.toString().length() == 1) {
                lastName = new StringBuilder(firstName + " " + lastName);
            }

            switch (passengerDto.getType()) {
                case ADULT -> {
                    map.put("titadt_" + (adultCounter.get()), passengerDto.getTitle().toString());
                    map.put("fnadt_" + (adultCounter.get()), firstName);
                    map.put("lnadt_" + (adultCounter.get()), lastName.toString());
                    map.put("hpadt_" + (adultCounter.get()), getCptlp());
                    if (passengerDto.getPhoneNumber() != null) {
                        map.put("hpadt_" + (adultCounter.get()), passengerDto.getPhoneNumber());
                    }
                    map.put("birthadt_" + (adultCounter.get()), (passengerDto.getBirthDate() != null) ? passengerDto.getBirthDate() : null);

                    if (passengerDto.getIdentityCard() != null) {
                        map.put("nikadt_" + (adultCounter.get()), passengerDto.getIdentityCard().getIdCardNumber());
                    }
                    if (passengerDto.getPassport() != null) {
                        map.put("passnoadt_" + (adultCounter.get()), passengerDto.getPassport().getPassportNumber());
                        map.put("passnatadt_" + (adultCounter.get()), passengerDto.getPassport().getIssueCountry());
                        map.put("natadt_" + (adultCounter.get()), passengerDto.getPassport().getIssueCountry());
                        map.put("passenddateadt_" + (adultCounter.get()), passengerDto.getPassport().getExpiryDate().toString());
                    }
                    adultCounter.getAndIncrement();
                }
                case CHILD -> {
                    map.put("titchd_" + (childCounter.get()), passengerDto.getTitle().toString());
                    map.put("fnchd_" + (childCounter.get()), firstName);
                    map.put("lnchd_" + (childCounter.get()), lastName.toString());
//                    map.put("hpchd_" + (childCounter.get()), passengerDto.getPhoneNumber());
                    map.put("birthchd_" + (childCounter.get()), passengerDto.getBirthDate());
                    if (passengerDto.getIdentityCard() != null) {
                        map.put("nikchd_" + (childCounter.get()), passengerDto.getIdentityCard().getIdCardNumber());
                    }
                    if (passengerDto.getPassport() != null) {
                        map.put("passnochd_" + (childCounter.get()), passengerDto.getPassport().getPassportNumber());
                        map.put("passnatchd_" + (childCounter.get()), passengerDto.getPassport().getIssueCountry());
                        map.put("natchd_" + (childCounter.get()), passengerDto.getPassport().getIssueCountry());
                        map.put("passenddatechd_" + (childCounter.get()), passengerDto.getPassport().getExpiryDate().toString());
                    }
                    childCounter.getAndIncrement();
                }
                case INFANT -> {
                    map.put("titinf_" + (infantCounter.get()), passengerDto.getTitle().toString());
                    map.put("fninf_" + (infantCounter.get()), firstName);
                    map.put("lninf_" + (infantCounter.get()), lastName.toString());
//                    map.put("hpinf_" + (infantCounter.get()), passengerDto.getPhoneNumber());
                    map.put("birthinf_" + (infantCounter.get()), passengerDto.getBirthDate());
                    if (passengerDto.getIdentityCard() != null) {
                        map.put("nikinf_" + (infantCounter.get()), passengerDto.getIdentityCard().getIdCardNumber());
                    }
                    if (passengerDto.getPassport() != null) {
                        map.put("passnoinf_" + (infantCounter.get()), passengerDto.getPassport().getPassportNumber());
                        map.put("passnatinf_" + (infantCounter.get()), passengerDto.getPassport().getIssueCountry());
                        map.put("natinf_" + (infantCounter.get()), passengerDto.getPassport().getIssueCountry());
                        map.put("passenddateinf_" + (infantCounter.get()), passengerDto.getPassport().getExpiryDate().toString());
                    }
                    infantCounter.getAndIncrement();
                }
            }
        }
        return map;
    }
}
