package id.holigo.services.holigoairlinesservice.services.logs;

import id.holigo.services.holigoairlinesservice.web.model.SupplierLogDto;

public interface LogService {
    void sendSupplierLog(SupplierLogDto supplierLogDto);
}
