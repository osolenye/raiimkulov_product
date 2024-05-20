package com.example.shops.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.shops.R;
import com.example.shops.databinding.FragmentHomeBinding;
import com.example.shops.models.ModelM;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    JemAdapter adapter;
    HomeViewModel homeViewModel;
    NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getModelMResponseLiveData().observe(getViewLifecycleOwner(), new Observer<List<ModelM>>() {
            @Override
            public void onChanged(List<ModelM> modelMS) {
                binding.progressBar.setVisibility(View.INVISIBLE);

                adapter = new JemAdapter(requireActivity(), modelMS);
                binding.rvCatalogM.setAdapter(adapter);
            }
        });
        setUpOnBackPressed();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.basketBtn.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireActivity(), binding.basketBtn);
            popup.getMenuInflater().inflate(R.menu.action_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getTitle().toString()) {
                        case "Добавить в корзину":
                            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("keysss_basket", adapter.getSelected_intoBasketList());
                            navController.navigate(R.id.navigation_basket, bundle);
                            break;
                        case "Пометить":
                            Toast.makeText(requireContext(), "Marked", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(requireContext(), "default", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            popup.show();
        });
    }

    private void setUpOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    requireActivity().finish();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}