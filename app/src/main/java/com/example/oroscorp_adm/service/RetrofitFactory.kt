package com.example.oroscorp_adm.service

import com.example.oroscorp_adm.model.Funcionario
import com.example.oroscorp_adm.model.OcorrenciaEpi
import com.example.oroscorp_adm.model.SetorEpi
import com.example.oroscorp_adm.model.StatusSetor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    private val BASE_URL = "http://localhost:8080/v1/oroscorp/"

    private val retrofitFactory = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun GetAdministradorService(): AdministradorService{
        return retrofitFactory.create(AdministradorService::class.java)
    }

    fun GetCameraService(): CameraService{
        return retrofitFactory.create(CameraService::class.java)
    }

    fun GetCargoService(): CargoService{
        return retrofitFactory.create(CargoService::class.java)
    }

    fun GetEpiService(): EpiService{
        return retrofitFactory.create(EpiService::class.java)
    }

    fun GetFuncionarioService(): FuncionarioService{
        return retrofitFactory.create(FuncionarioService::class.java)
    }

    fun GetMotivoOcorrenciaService(): MotivoOcorrenciaService{
        return retrofitFactory.create(MotivoOcorrenciaService::class.java)
    }

    fun GetNivelAcessoService(): NivelAcessoService{
        return retrofitFactory.create(NivelAcessoService::class.java)
    }

    fun GetOcorrenciaCameraService(): OcorrenciaCameraService{
        return retrofitFactory.create(OcorrenciaCameraService::class.java)
    }

    fun GetOcorrenciaEpiService(): OcorrenciaEpiService{
        return retrofitFactory.create(OcorrenciaEpiService::class.java)
    }

    fun GetOcorrenciaService(): OcorrenciaService{
        return retrofitFactory.create(OcorrenciaService::class.java)
    }

    fun GetResponsavelService(): ResponsavelService{
        return retrofitFactory.create(ResponsavelService::class.java)
    }

    fun GetSetorEpiService(): SetorEpiService{
        return retrofitFactory.create(SetorEpiService::class.java)
    }

    fun GetSetorFuncionarioService(): SetorFuncionarioService{
        return retrofitFactory.create(SetorFuncionarioService::class.java)
    }

    fun GetSetorResponsavelService(): SetorResponsavelService{
        return retrofitFactory.create(SetorResponsavelService::class.java)
    }

    fun GetSetorService(): SetorService{
        return retrofitFactory.create(SetorService::class.java)
    }

    fun GetStatusCameraService(): StatusCameraService{
        return retrofitFactory.create(StatusCameraService::class.java)
    }

    fun GetStatusContaService(): StatusContaService{
        return retrofitFactory.create(StatusContaService::class.java)
    }

    fun GetStatusFuncionarioService(): StatusFuncionarioService{
        return retrofitFactory.create(StatusFuncionarioService::class.java)
    }

    fun GetStatusOcorrenciaService(): StatusOcorrenciaService{
        return retrofitFactory.create(StatusOcorrenciaService::class.java)
    }

    fun GetStatusSetorService(): StatusSetorService{
        return retrofitFactory.create(StatusSetorService::class.java)
    }

    fun GetUsuarioService(): UsuarioService{
        return retrofitFactory.create(UsuarioService::class.java)
    }

}