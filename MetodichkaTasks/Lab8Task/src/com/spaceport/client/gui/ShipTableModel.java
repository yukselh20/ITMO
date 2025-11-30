package com.spaceport.client.gui;

import com.spaceport.client.util.ResourceManager;
import com.spaceport.common.Spaceship;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ShipTableModel extends AbstractTableModel {
    private List<Spaceship> ships = new ArrayList<>();
    private final String[] columnKeys = {
            "table.column.id",
            "table.column.name",
            "table.column.tonnage",
            "table.column.type",
            "table.column.owner",
            "table.column.x",
            "table.column.y"
    };

    public void setShips(List<Spaceship> ships) {
        this.ships = ships;
        fireTableDataChanged();
    }

    public Spaceship getShipAt(int rowIndex) {
        return ships.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return ships.size();
    }

    @Override
    public int getColumnCount() {
        return columnKeys.length;
    }

    @Override
    public String getColumnName(int column) {
        return ResourceManager.get(columnKeys[column]);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Spaceship ship = ships.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return ship.getId();
            case 1:
                return ship.getName();
            case 2:
                return ship.getTonnage();
            case 3:
                return ship.getType();
            case 4:
                return ship.getOwnerId();
            case 5:
                return ship.getX();
            case 6:
                return ship.getY();
            default:
                return null;
        }
    }
}
