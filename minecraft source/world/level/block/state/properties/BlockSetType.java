/*     */ package net.minecraft.world.level.block.state.properties;
/*     */ 
/*     */ public final class BlockSetType extends Record {
/*     */   private final String name;
/*     */   private final boolean canOpenByHand;
/*     */   private final boolean canOpenByWindCharge;
/*     */   private final boolean canButtonBeActivatedByArrows;
/*     */   private final PressurePlateSensitivity pressurePlateSensitivity;
/*     */   private final SoundType soundType;
/*     */   private final SoundEvent doorClose;
/*     */   
/*  12 */   public BlockSetType(String name, boolean canOpenByHand, boolean canOpenByWindCharge, boolean canButtonBeActivatedByArrows, PressurePlateSensitivity pressurePlateSensitivity, SoundType soundType, SoundEvent doorClose, SoundEvent doorOpen, SoundEvent trapdoorClose, SoundEvent trapdoorOpen, SoundEvent pressurePlateClickOff, SoundEvent pressurePlateClickOn, SoundEvent buttonClickOff, SoundEvent buttonClickOn) { this.name = name; this.canOpenByHand = canOpenByHand; this.canOpenByWindCharge = canOpenByWindCharge; this.canButtonBeActivatedByArrows = canButtonBeActivatedByArrows; this.pressurePlateSensitivity = pressurePlateSensitivity; this.soundType = soundType; this.doorClose = doorClose; this.doorOpen = doorOpen; this.trapdoorClose = trapdoorClose; this.trapdoorOpen = trapdoorOpen; this.pressurePlateClickOff = pressurePlateClickOff; this.pressurePlateClickOn = pressurePlateClickOn; this.buttonClickOff = buttonClickOff; this.buttonClickOn = buttonClickOn; } private final SoundEvent doorOpen; private final SoundEvent trapdoorClose; private final SoundEvent trapdoorOpen; private final SoundEvent pressurePlateClickOff; private final SoundEvent pressurePlateClickOn; private final SoundEvent buttonClickOff; private final SoundEvent buttonClickOn; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/state/properties/BlockSetType;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/BlockSetType; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/state/properties/BlockSetType;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/BlockSetType; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/state/properties/BlockSetType;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/state/properties/BlockSetType;
/*  12 */     //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public boolean canOpenByHand() { return this.canOpenByHand; } public boolean canOpenByWindCharge() { return this.canOpenByWindCharge; } public boolean canButtonBeActivatedByArrows() { return this.canButtonBeActivatedByArrows; } public PressurePlateSensitivity pressurePlateSensitivity() { return this.pressurePlateSensitivity; } public SoundType soundType() { return this.soundType; } public SoundEvent doorClose() { return this.doorClose; } public SoundEvent doorOpen() { return this.doorOpen; } public SoundEvent trapdoorClose() { return this.trapdoorClose; } public SoundEvent trapdoorOpen() { return this.trapdoorOpen; } public SoundEvent pressurePlateClickOff() { return this.pressurePlateClickOff; } public SoundEvent pressurePlateClickOn() { return this.pressurePlateClickOn; } public SoundEvent buttonClickOff() { return this.buttonClickOff; } public SoundEvent buttonClickOn() { return this.buttonClickOn; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  28 */   private static final Map<String, BlockSetType> TYPES = new Object2ObjectArrayMap(); public static final Codec<BlockSetType> CODEC; public static final BlockSetType IRON; public static final BlockSetType COPPER; public static final BlockSetType GOLD; public static final BlockSetType STONE; public static final BlockSetType POLISHED_BLACKSTONE; public static final BlockSetType OAK; public static final BlockSetType SPRUCE; public static final BlockSetType BIRCH; static  {
/*  29 */     Objects.requireNonNull(TYPES); CODEC = Codec.stringResolver(BlockSetType::name, TYPES::get);
/*     */     
/*  31 */     IRON = register(new BlockSetType("iron", false, false, false, PressurePlateSensitivity.EVERYTHING, SoundType.IRON, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  50 */     COPPER = register(new BlockSetType("copper", true, true, false, PressurePlateSensitivity.EVERYTHING, SoundType.COPPER, SoundEvents.COPPER_DOOR_CLOSE, SoundEvents.COPPER_DOOR_OPEN, SoundEvents.COPPER_TRAPDOOR_CLOSE, SoundEvents.COPPER_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     GOLD = register(new BlockSetType("gold", false, true, false, PressurePlateSensitivity.EVERYTHING, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     STONE = register(new BlockSetType("stone", true, true, false, PressurePlateSensitivity.MOBS, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     POLISHED_BLACKSTONE = register(new BlockSetType("polished_blackstone", true, true, false, PressurePlateSensitivity.MOBS, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     OAK = register(new BlockSetType("oak"));
/* 140 */     SPRUCE = register(new BlockSetType("spruce"));
/* 141 */     BIRCH = register(new BlockSetType("birch"));
/* 142 */     ACACIA = register(new BlockSetType("acacia"));
/* 143 */     CHERRY = register(new BlockSetType("cherry", true, true, true, PressurePlateSensitivity.EVERYTHING, SoundType.CHERRY_WOOD, SoundEvents.CHERRY_WOOD_DOOR_CLOSE, SoundEvents.CHERRY_WOOD_DOOR_OPEN, SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE, SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN, SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.CHERRY_WOOD_BUTTON_CLICK_OFF, SoundEvents.CHERRY_WOOD_BUTTON_CLICK_ON));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     JUNGLE = register(new BlockSetType("jungle"));
/* 160 */     DARK_OAK = register(new BlockSetType("dark_oak"));
/* 161 */     PALE_OAK = register(new BlockSetType("pale_oak"));
/* 162 */     CRIMSON = register(new BlockSetType("crimson", true, true, true, PressurePlateSensitivity.EVERYTHING, SoundType.NETHER_WOOD, SoundEvents.NETHER_WOOD_DOOR_CLOSE, SoundEvents.NETHER_WOOD_DOOR_OPEN, SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE, SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF, SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     WARPED = register(new BlockSetType("warped", true, true, true, PressurePlateSensitivity.EVERYTHING, SoundType.NETHER_WOOD, SoundEvents.NETHER_WOOD_DOOR_CLOSE, SoundEvents.NETHER_WOOD_DOOR_OPEN, SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE, SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF, SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     MANGROVE = register(new BlockSetType("mangrove"));
/* 195 */     BAMBOO = register(new BlockSetType("bamboo", true, true, true, PressurePlateSensitivity.EVERYTHING, SoundType.BAMBOO_WOOD, SoundEvents.BAMBOO_WOOD_DOOR_CLOSE, SoundEvents.BAMBOO_WOOD_DOOR_OPEN, SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE, SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN, SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_OFF, SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final BlockSetType ACACIA;
/*     */   
/*     */   public static final BlockSetType CHERRY;
/*     */   
/*     */   public static final BlockSetType JUNGLE;
/*     */   
/*     */   public static final BlockSetType DARK_OAK;
/*     */   
/*     */   public static final BlockSetType PALE_OAK;
/*     */   public static final BlockSetType CRIMSON;
/*     */   public static final BlockSetType WARPED;
/*     */   public static final BlockSetType MANGROVE;
/*     */   public static final BlockSetType BAMBOO;
/*     */   
/* 213 */   public BlockSetType(String name) { this(name, true, true, true, PressurePlateSensitivity.EVERYTHING, SoundType.WOOD, SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static BlockSetType register(BlockSetType type) {
/* 232 */     TYPES.put(type.name, type);
/* 233 */     return type;
/*     */   }
/*     */ 
/*     */   
/* 237 */   public static Stream<BlockSetType> values() { return TYPES.values().stream(); }
/*     */   
/*     */   public enum PressurePlateSensitivity
/*     */   {
/* 241 */     EVERYTHING, MOBS;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\BlockSetType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */