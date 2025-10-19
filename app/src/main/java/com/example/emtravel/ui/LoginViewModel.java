package com.example.emtravel.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;


public class LoginViewModel extends ViewModel {

    private UsuarioRepository usuarioRepository;
    private MediatorLiveData<String> statusLogin = new MediatorLiveData<>();

    public LoginViewModel() {
        usuarioRepository = new UsuarioRepository();
    }

    public LiveData<String> getStatsLogin() {
        return statusLogin;
    }

    public void autenticarUsuario(String email, String senha) {
        if (email.isEmpty() || senha.isEmpty()) {
            statusLogin.setValue("Preencha todos os campos");
            return;
        }

        LiveData<String> resultadoLogin = usuarioRepository.loginUsuario(email, senha);
        statusLogin.addSource(resultadoLogin, status -> {
            if ("success".equals(status)) {
                statusLogin.postValue("Login realizado com sucesso!");
            } else {
                statusLogin.postValue(status);
            }
            statusLogin.removeSource(resultadoLogin); // remove a source para não receber mais updates
        });
    }

    public boolean isUsuarioLogado() {
        return usuarioRepository.usuarioLogado();
    }
}
