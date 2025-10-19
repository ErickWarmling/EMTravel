package com.example.emtravel.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.emtravel.model.Usuario;

public class LoginViewModel extends ViewModel {

    private UsuarioRepository usuarioRepository;

    public LoginViewModel() {
        usuarioRepository = new UsuarioRepository();
    }

    public LiveData<String> login(String email, String senha) {
        return usuarioRepository.loginUsuario(email, senha);
    }

}
