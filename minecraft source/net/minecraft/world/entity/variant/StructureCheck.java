/*    */ package net.minecraft.world.entity.variant;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ 
/*    */ public final class StructureCheck extends Record implements SpawnCondition {
/* 10 */   public StructureCheck(HolderSet<Structure> requiredStructures) { this.requiredStructures = requiredStructures; } private final HolderSet<Structure> requiredStructures; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/StructureCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/StructureCheck; } public HolderSet<Structure> requiredStructures() { return this.requiredStructures; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/StructureCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/StructureCheck; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/StructureCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/StructureCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 14 */   public static final MapCodec<StructureCheck> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 15 */         RegistryCodecs.homogeneousList(Registries.STRUCTURE).fieldOf("structures").forGetter(StructureCheck::requiredStructures))
/* 16 */       .apply(i, StructureCheck::new));
/*    */ 
/*    */ 
/*    */   
/* 20 */   public boolean test(SpawnContext context) { return context.level().getLevel().structureManager().getStructureWithPieceAt(context.pos(), this.requiredStructures).isValid(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public MapCodec<StructureCheck> codec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\variant\StructureCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */