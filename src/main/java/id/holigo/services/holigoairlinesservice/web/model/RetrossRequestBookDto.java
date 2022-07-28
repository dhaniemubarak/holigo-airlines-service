package id.holigo.services.holigoairlinesservice.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
public class RetrossRequestBookDto implements Serializable {
    private String rqid;
    private String mmid;
    private String app;
    private String action;
    private String cpname;
    private String cpmail;
    private String cptlp;
    private String org;
    private String des;
    private String trip;
    private Integer adt;
    private Integer chd;
    private Integer inf;
    private String tgl_dep;
    private String selectedIdDep;
    private String tgl_ret;
    private String selectedIdRet;
    private List<PassengerDto> passengers;

    HashMap<String, Object> map = new HashMap<>();

    public Map<String, Object> build() {

        map.put("rqid", getRqid());
        map.put("mmid", getMmid());
        map.put("app", getApp());
        map.put("action", getAction());
        map.put("cpname", getCpname());
        map.put("cpmail", getCpmail());
        map.put("cptlp", getCptlp());
        map.put("org", getOrg());
        map.put("des", getDes());
        map.put("trip", getTrip());
        map.put("adt", getAdt());
        map.put("chd", getChd());
        map.put("inf", getInf());
        map.put("tgl_dep", getTgl_dep());
        map.put("selectedIDdep", getSelectedIdDep());
        if (getTrip().equals("R")) {
            map.put("tgl_ret", getTgl_ret());
            map.put("selectedIDret", getSelectedIdRet());
        }
        for (int i = 0; i < getPassengers().size(); i++) {
            System.out.println("Index -> " + i);
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
                lastName = new StringBuilder(firstName + " " + lastName.toString());
            }

            switch (passengerDto.getType()) {
                case ADULT -> {
                    map.put("titadt_" + (i + 1), passengerDto.getTitle().toString());
                    map.put("fnadt_" + (i + 1), firstName);
                    map.put("lnadt_" + (i + 1), lastName.toString());
                    map.put("hpadt_" + (i + 1), passengerDto.getPhoneNumber());
                    map.put("birthadt_" + (i + 1), passengerDto.getBirthDate().toString());

                    if (passengerDto.getIdentityCard() != null) {
                        map.put("nikadt_" + (i + 1), passengerDto.getIdentityCard().getIdCardNumber());
                    }
                    if (passengerDto.getPassport() != null) {
                        map.put("passnoadt_" + (i + 1), passengerDto.getPassport().getPassportNumber());
                        map.put("passnatadt_" + (i + 1), passengerDto.getPassport().getIssueCountry());
                        map.put("natadt_" + (i + 1), passengerDto.getPassport().getIssueCountry());
                        map.put("passenddateadt_" + (i + 1), passengerDto.getPassport().getExpiryDate().toString());
                    }
                }
                case CHILD -> {
                    map.put("titchd_" + (i + 1), passengerDto.getTitle().toString());
                    map.put("fnchd_" + (i + 1), firstName);
                    map.put("lnchd_" + (i + 1), lastName.toString());
                    map.put("hpchd_" + (i + 1), passengerDto.getPhoneNumber());
                    map.put("birthchd_" + (i + 1), passengerDto.getBirthDate().toString());
                    if (passengerDto.getIdentityCard() != null) {
                        map.put("nikadt_" + (i + 1), passengerDto.getIdentityCard().getIdCardNumber());
                    }
                    if (passengerDto.getPassport() != null) {
                        map.put("passnochd_" + (i + 1), passengerDto.getPassport().getPassportNumber());
                        map.put("passnatchd_" + (i + 1), passengerDto.getPassport().getIssueCountry());
                        map.put("natchd_" + (i + 1), passengerDto.getPassport().getIssueCountry());
                        map.put("passenddatechd_" + (i + 1), passengerDto.getPassport().getExpiryDate().toString());
                    }
                }
                case INFANT -> {
                    map.put("titinf_" + (i + 1), passengerDto.getTitle().toString());
                    map.put("fninf_" + (i + 1), firstName);
                    map.put("lninf_" + (i + 1), lastName.toString());
                    map.put("hpinf_" + (i + 1), passengerDto.getPhoneNumber());
                    map.put("birthinf_" + (i + 1), passengerDto.getBirthDate().toString());
                    if (passengerDto.getIdentityCard() != null) {
                        map.put("nikinf_" + (i + 1), passengerDto.getIdentityCard().getIdCardNumber());
                    }
                    if (passengerDto.getPassport() != null) {
                        map.put("passnoinf_" + (i + 1), passengerDto.getPassport().getPassportNumber());
                        map.put("passnatinf_" + (i + 1), passengerDto.getPassport().getIssueCountry());
                        map.put("natinf_" + (i + 1), passengerDto.getPassport().getIssueCountry());
                        map.put("passenddateinf_" + (i + 1), passengerDto.getPassport().getExpiryDate().toString());
                    }
                }
            }
        }
        return map;
    }
}
