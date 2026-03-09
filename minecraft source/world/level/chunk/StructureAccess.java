package net.minecraft.world.level.chunk;

import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Map;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

public interface StructureAccess {
  StructureStart getStartForStructure(Structure paramStructure);
  
  void setStartForStructure(Structure paramStructure, StructureStart paramStructureStart);
  
  LongSet getReferencesForStructure(Structure paramStructure);
  
  void addReferenceForStructure(Structure paramStructure, long paramLong);
  
  Map<Structure, LongSet> getAllReferences();
  
  void setAllReferences(Map<Structure, LongSet> paramMap);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\StructureAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */