/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.level.block.SoundType;
/*    */ 
/*    */ public final class WoodType extends Record {
/*    */   private final String name;
/*    */   private final BlockSetType setType;
/*    */   private final SoundType soundType;
/*    */   
/* 12 */   public WoodType(String name, BlockSetType setType, SoundType soundType, SoundType hangingSignSoundType, SoundEvent fenceGateClose, SoundEvent fenceGateOpen) { this.name = name; this.setType = setType; this.soundType = soundType; this.hangingSignSoundType = hangingSignSoundType; this.fenceGateClose = fenceGateClose; this.fenceGateOpen = fenceGateOpen; } private final SoundType hangingSignSoundType; private final SoundEvent fenceGateClose; private final SoundEvent fenceGateOpen; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/state/properties/WoodType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/WoodType; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/state/properties/WoodType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/WoodType; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/state/properties/WoodType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/state/properties/WoodType;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public BlockSetType setType() { return this.setType; } public SoundType soundType() { return this.soundType; } public SoundType hangingSignSoundType() { return this.hangingSignSoundType; } public SoundEvent fenceGateClose() { return this.fenceGateClose; } public SoundEvent fenceGateOpen() { return this.fenceGateOpen; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   private static final Map<String, WoodType> TYPES = new Object2ObjectArrayMap(); public static final Codec<WoodType> CODEC; public static final WoodType OAK; public static final WoodType SPRUCE; public static final WoodType BIRCH; public static final WoodType ACACIA; public static final WoodType CHERRY; static  {
/* 21 */     Objects.requireNonNull(TYPES); CODEC = Codec.stringResolver(WoodType::name, TYPES::get);
/*    */     
/* 23 */     OAK = register(new WoodType("oak", BlockSetType.OAK));
/* 24 */     SPRUCE = register(new WoodType("spruce", BlockSetType.SPRUCE));
/* 25 */     BIRCH = register(new WoodType("birch", BlockSetType.BIRCH));
/* 26 */     ACACIA = register(new WoodType("acacia", BlockSetType.ACACIA));
/* 27 */     CHERRY = register(new WoodType("cherry", BlockSetType.CHERRY, SoundType.CHERRY_WOOD, SoundType.CHERRY_WOOD_HANGING_SIGN, SoundEvents.CHERRY_WOOD_FENCE_GATE_CLOSE, SoundEvents.CHERRY_WOOD_FENCE_GATE_OPEN));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     JUNGLE = register(new WoodType("jungle", BlockSetType.JUNGLE));
/* 36 */     DARK_OAK = register(new WoodType("dark_oak", BlockSetType.DARK_OAK));
/* 37 */     PALE_OAK = register(new WoodType("pale_oak", BlockSetType.PALE_OAK));
/* 38 */     CRIMSON = register(new WoodType("crimson", BlockSetType.CRIMSON, SoundType.NETHER_WOOD, SoundType.NETHER_WOOD_HANGING_SIGN, SoundEvents.NETHER_WOOD_FENCE_GATE_CLOSE, SoundEvents.NETHER_WOOD_FENCE_GATE_OPEN));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     WARPED = register(new WoodType("warped", BlockSetType.WARPED, SoundType.NETHER_WOOD, SoundType.NETHER_WOOD_HANGING_SIGN, SoundEvents.NETHER_WOOD_FENCE_GATE_CLOSE, SoundEvents.NETHER_WOOD_FENCE_GATE_OPEN));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 54 */     MANGROVE = register(new WoodType("mangrove", BlockSetType.MANGROVE));
/* 55 */     BAMBOO = register(new WoodType("bamboo", BlockSetType.BAMBOO, SoundType.BAMBOO_WOOD, SoundType.BAMBOO_WOOD_HANGING_SIGN, SoundEvents.BAMBOO_WOOD_FENCE_GATE_CLOSE, SoundEvents.BAMBOO_WOOD_FENCE_GATE_OPEN));
/*    */   }
/*    */   public static final WoodType JUNGLE;
/*    */   public static final WoodType DARK_OAK;
/*    */   public static final WoodType PALE_OAK;
/*    */   public static final WoodType CRIMSON;
/*    */   public static final WoodType WARPED;
/*    */   public static final WoodType MANGROVE;
/*    */   public static final WoodType BAMBOO;
/*    */   
/* 65 */   public WoodType(String name, BlockSetType setType) { this(name, setType, SoundType.WOOD, SoundType.HANGING_SIGN, SoundEvents.FENCE_GATE_CLOSE, SoundEvents.FENCE_GATE_OPEN); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static WoodType register(WoodType type) {
/* 76 */     TYPES.put(type.name(), type);
/* 77 */     return type;
/*    */   }
/*    */ 
/*    */   
/* 81 */   public static Stream<WoodType> values() { return TYPES.values().stream(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\WoodType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */