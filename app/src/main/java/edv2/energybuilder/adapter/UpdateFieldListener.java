package edv2.energybuilder.adapter;

import android.view.View;

import edv2.energybuilder.model.ObjectField;

public interface UpdateFieldListener {
    void changeValue(int position, ObjectField objectField);
}
