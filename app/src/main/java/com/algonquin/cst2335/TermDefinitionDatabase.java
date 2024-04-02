package com.algonquin.cst2335;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TermDefinition.class}, version = 1)
public abstract class TermDefinitionDatabase extends RoomDatabase {
    public abstract TermDefinitionDAO dDAO();
}