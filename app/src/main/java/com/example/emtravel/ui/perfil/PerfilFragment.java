package com.example.emtravel.ui.perfil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.emtravel.R;
import com.example.emtravel.ui.login.LoginActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executor;

public class PerfilFragment extends Fragment {

    private PerfilViewModel perfilViewModel;
    private ImageView imgFotoPerfil;
    private ImageButton btnEditarFoto, btnRemoverFoto;
    private TextView tvNomeUsuario, tvEmailUsuario, tvDataNascimentoUsuario, tvTelefoneUsuario;
    private Button btnSair;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private String nomeArquivoFoto;

    // Overlay escuro durante a biometria
    private View overlayView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Referências
        imgFotoPerfil = view.findViewById(R.id.imgFotoPerfil);
        btnEditarFoto = view.findViewById(R.id.btnEditarFoto);
        btnRemoverFoto = view.findViewById(R.id.btnRemoverFoto);
        tvNomeUsuario = view.findViewById(R.id.tvNomeUsuario);
        tvEmailUsuario = view.findViewById(R.id.tvEmailUsuario);
        tvDataNascimentoUsuario = view.findViewById(R.id.tvDataNascimentoUsuario);
        tvTelefoneUsuario = view.findViewById(R.id.tvTelefoneUsuario);
        btnSair = view.findViewById(R.id.btnSair);

        // Inicializa ViewModel
        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        // Autenticação biométrica ao abrir o perfil
        verificarBiometria();

        // Observa dados do usuário
        perfilViewModel.getUsuario().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                tvNomeUsuario.setText(usuario.getNome());
                tvEmailUsuario.setText(usuario.getEmail());
                tvDataNascimentoUsuario.setText(usuario.getDataNascimento());
                tvTelefoneUsuario.setText(usuario.getTelefone());

                nomeArquivoFoto = "foto_perfil_" + usuario.getUId() + ".jpg";
                carregarFotoLocal();
            }
        });

        // Botão sair
        btnSair.setOnClickListener(v -> {
            perfilViewModel.sair();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });

        // Configura câmera
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bitmap foto = (Bitmap) result.getData().getExtras().get("data");
                        if (foto != null) {
                            imgFotoPerfil.setImageBitmap(foto);
                            salvarFotoLocal(foto);
                            Toast.makeText(requireContext(), "Foto de perfil atualizada!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Clique para abrir a câmera
        btnEditarFoto.setOnClickListener(v -> verificarPermissaoCamera());

        // Clique para remover foto com confirmação
        btnRemoverFoto.setOnClickListener(v -> confirmarRemocaoFoto());

        return view;
    }

    /** Verifica biometria disponível e solicita autenticação */
    private void verificarBiometria() {
        BiometricManager biometricManager = BiometricManager.from(requireContext());
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                mostrarDialogoBiometrico();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(requireContext(), "Sem sensor biométrico disponível", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(requireContext(), "Sensor biométrico indisponível", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(requireContext(), "Nenhuma digital cadastrada", Toast.LENGTH_LONG).show();
                break;
        }
    }

    /** Mostra prompt biométrico com overlay escuro */
    private void mostrarDialogoBiometrico() {
        ViewGroup root = requireActivity().findViewById(android.R.id.content);
        mostrarOverlay(root);

        Executor executor = ContextCompat.getMainExecutor(requireContext());
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                removerOverlay(root);
                Toast.makeText(requireContext(), "Autenticação bem-sucedida!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(requireContext(), "Falha na autenticação, tente novamente", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                removerOverlay(root);
                Toast.makeText(requireContext(), "Erro: " + errString, Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Confirmação biométrica")
                .setSubtitle("Use sua digital para acessar o perfil")
                .setNegativeButtonText("Cancelar")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    /** Mostra overlay escuro */
    private void mostrarOverlay(ViewGroup container) {
        overlayView = new View(requireContext());
        overlayView.setBackgroundColor(Color.parseColor("#CC000000")); // Preto semi-transparente
        container.addView(overlayView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
    }

    /** Remove overlay escuro */
    private void removerOverlay(ViewGroup container) {
        if (overlayView != null) {
            container.removeView(overlayView);
            overlayView = null;
        }
    }

    /** Verifica e solicita permissão da câmera */
    private void verificarPermissaoCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            abrirCamera();
        }
    }

    /** Abre a câmera */
    private void abrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            cameraLauncher.launch(intent);
        } else {
            Toast.makeText(requireContext(), "Câmera não disponível neste dispositivo", Toast.LENGTH_SHORT).show();
        }
    }

    /** Resultado da permissão */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamera();
            } else {
                Toast.makeText(requireContext(), "Permissão da câmera negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /** Exibe diálogo de confirmação antes de remover a foto */
    private void confirmarRemocaoFoto() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Remover foto de perfil")
                .setMessage("Deseja realmente remover sua foto de perfil?")
                .setPositiveButton("Remover", (dialog, which) -> removerFotoPerfil())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /** Remove a foto e restaura o ícone padrão */
    private void removerFotoPerfil() {
        if (nomeArquivoFoto == null) return;

        File arquivo = new File(requireContext().getFilesDir(), nomeArquivoFoto);
        if (arquivo.exists() && arquivo.delete()) {
            imgFotoPerfil.setImageResource(R.drawable.ic_perfil);
            Toast.makeText(requireContext(), "Foto de perfil removida!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Nenhuma foto para remover", Toast.LENGTH_SHORT).show();
        }
    }

    /** Salva a foto no armazenamento interno */
    private void salvarFotoLocal(Bitmap bitmap) {
        if (nomeArquivoFoto == null) return;
        File arquivo = new File(requireContext().getFilesDir(), nomeArquivoFoto);
        try (FileOutputStream fos = new FileOutputStream(arquivo)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erro ao salvar foto", Toast.LENGTH_SHORT).show();
        }
    }

    /** Carrega a foto salva, se existir */
    private void carregarFotoLocal() {
        if (nomeArquivoFoto == null) return;
        File arquivo = new File(requireContext().getFilesDir(), nomeArquivoFoto);
        if (arquivo.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(arquivo.getAbsolutePath());
            imgFotoPerfil.setImageBitmap(bitmap);
        } else {
            imgFotoPerfil.setImageResource(R.drawable.ic_perfil);
        }
    }
}
