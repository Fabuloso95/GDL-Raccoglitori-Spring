package com.gdl_raccoglitori.facade;

import com.gdl_raccoglitori.dto.request.FrasePreferitaRequest;
import com.gdl_raccoglitori.dto.response.FrasePreferitaResponse;
import java.util.*;

public interface FrasePreferitaFacade
{
	FrasePreferitaResponse saveFrase(FrasePreferitaRequest request);
	void deleteFrase(Long fraseId);
	FrasePreferitaResponse findById(Long id);
	List<FrasePreferitaResponse> findMyFrasi();
	List<FrasePreferitaResponse> findByLibroId(Long libroId);
}
