package com.example.emtravel.ui.perfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.emtravel.model.Usuario;
import com.example.emtravel.service.UsuarioRepository;
import com.google.firebase.auth.FirebaseAuth;

public class PerfilViewModel extends ViewModel {
    private UsuarioRepository usuarioRepository;
    private LiveData<Usuario> usuario;

    public PerfilViewModel() {
        usuarioRepository = new UsuarioRepository();
        usuario = usuarioRepository.getUsuarioLogado();
    }

    public LiveData<Usuario> getUsuario() {
        return usuario;
    }

    public void sair() {
        FirebaseAuth.getInstance().signOut();

    }
}