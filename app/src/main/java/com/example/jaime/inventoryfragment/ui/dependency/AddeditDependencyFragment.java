package com.example.jaime.inventoryfragment.ui.dependency;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jaime.inventoryfragment.R;
import com.example.jaime.inventoryfragment.data.db.model.Dependency;
import com.example.jaime.inventoryfragment.ui.base.BaseFragment;
import com.example.jaime.inventoryfragment.ui.base.BasePresenter;
import com.example.jaime.inventoryfragment.ui.dependency.contracts.AddeditDependencyContract;
import com.example.jaime.inventoryfragment.ui.utils.AddEdit;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddeditDependencyFragment extends BaseFragment implements AddeditDependencyContract.View,
        TextWatcher {
    public static final String TAG = "addeditdependency";
    public static final String EDIT_KEY = "edit";

    private AddeditDependencyContract.Presenter mPresenter;
    private AddeditDependencyListener mCallback;
    private AddEdit mMode;

    private FloatingActionButton fabDependency;
    private TextInputLayout tilName;
    private TextInputLayout tilSortName;
    private TextInputLayout tilDescription;
    private EditText edtName;
    private EditText edtSortname;
    private EditText edtDescription;

    interface AddeditDependencyListener {


        void listDependency();
    }
    public static AddeditDependencyFragment newInstance(Bundle bundle) {
        AddeditDependencyFragment addeditDependencyFragment = new AddeditDependencyFragment();

        if (bundle != null)
            addeditDependencyFragment.setArguments(bundle);

        return addeditDependencyFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (AddeditDependencyListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getLocalClassName() + " must implements ListDepedencyListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addedit_dependency, container, false);

        fabDependency = (FloatingActionButton) root.findViewById(R.id.fab_dependency_save);
        tilName = (TextInputLayout) root.findViewById(R.id.til_dependency_name);
        edtName = (EditText) root.findViewById(R.id.edt_dependency_name);
        edtName.addTextChangedListener(this);

        tilSortName = (TextInputLayout) root.findViewById(R.id.til_dependency_sortname);
        edtSortname = (EditText) root.findViewById(R.id.edt_dependency_sortname);
        edtSortname.addTextChangedListener(this);

        tilDescription = (TextInputLayout) root.findViewById(R.id.til_dependency_description);
        edtDescription = (EditText) root.findViewById(R.id.edt_dependency_description);
        edtDescription.addTextChangedListener(this);

        if (getArguments() != null) {
            mMode = new AddEdit(AddEdit.EDIT_MODE);
        } else
            mMode = new AddEdit();

        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabDependency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMode.getMode() == AddEdit.ADD_MODE) {
                    mPresenter.saveDependency(
                            tilName.getEditText().getText().toString(),
                            tilSortName.getEditText().getText().toString(),
                            tilDescription.getEditText().getText().toString());

                } else if (mMode.getMode() == AddEdit.EDIT_MODE) {
                    Dependency dependency = getArguments().getParcelable(EDIT_KEY);
                    dependency.setDescription(edtDescription.getText().toString());
                    mPresenter.editDependency(dependency);
                }
            }
        });

        if (mMode.getMode() == AddEdit.EDIT_MODE) {
            Dependency dependency = getArguments().getParcelable(EDIT_KEY);

            edtName.setText(dependency.getName());
            edtName.setEnabled(false);

            edtSortname.setText(dependency.getShortname());
            edtSortname.setEnabled(false);

            edtDescription.setText(dependency.getDescription());
        }
    }


    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = (AddeditDependencyContract.Presenter) presenter;
    }


    @Override
    public void navigateToListDependency() {
        mCallback.listDependency();
    }


    @Override
    public void setNameEmptyError() {
        tilName.setError(getResources().getString(R.string.errorDependencyNameEmptyError));
    }


    @Override
    public void setSortNameEmptyError() {
        tilSortName.setError(getResources().getString(R.string.errorDependencySortNameEmptyError));
    }


    @Override
    public void setSortNameLengthError() {
        tilSortName.setError(getResources().getString(R.string.errorDependencySortNameLengthError));
    }


    @Override
    public void setDescriptionEmptyError() {
        tilDescription.setError(getResources().getString(R.string.errorDependencyDescriptionEmptyError));
    }


    @Override
    public void setValidateDependencyError() {
        onError(getActivity().findViewById(android.R.id.content), "Dependencia ya existe");
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tilName.setError(null);
        tilSortName.setError(null);
        tilDescription.setError(null);
    }


    @Override
    public void afterTextChanged(Editable s) {

    }
}
