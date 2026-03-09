/*     */ package net.minecraft.world.item;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.item.crafting.CraftingInput;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.level.material.MapColor;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ 
/*     */ public static enum DyeColor implements StringRepresentable {
/*  24 */   WHITE(0, "white", 16383998, MapColor.SNOW, 15790320, 16777215),
/*  25 */   ORANGE(1, "orange", 16351261, MapColor.COLOR_ORANGE, 15435844, 16738335),
/*  26 */   MAGENTA(2, "magenta", 13061821, MapColor.COLOR_MAGENTA, 12801229, 16711935),
/*  27 */   LIGHT_BLUE(3, "light_blue", 3847130, MapColor.COLOR_LIGHT_BLUE, 6719955, 10141901),
/*  28 */   YELLOW(4, "yellow", 16701501, MapColor.COLOR_YELLOW, 14602026, 16776960),
/*  29 */   LIME(5, "lime", 8439583, MapColor.COLOR_LIGHT_GREEN, 4312372, 12582656),
/*  30 */   PINK(6, "pink", 15961002, MapColor.COLOR_PINK, 14188952, 16738740),
/*  31 */   GRAY(7, "gray", 4673362, MapColor.COLOR_GRAY, 4408131, 8421504),
/*  32 */   LIGHT_GRAY(8, "light_gray", 10329495, MapColor.COLOR_LIGHT_GRAY, 11250603, 13882323),
/*  33 */   CYAN(9, "cyan", 1481884, MapColor.COLOR_CYAN, 2651799, 65535),
/*  34 */   PURPLE(10, "purple", 8991416, MapColor.COLOR_PURPLE, 8073150, 10494192),
/*  35 */   BLUE(11, "blue", 3949738, MapColor.COLOR_BLUE, 2437522, 255),
/*  36 */   BROWN(12, "brown", 8606770, MapColor.COLOR_BROWN, 5320730, 9127187),
/*  37 */   GREEN(13, "green", 6192150, MapColor.COLOR_GREEN, 3887386, 65280),
/*  38 */   RED(14, "red", 11546150, MapColor.COLOR_RED, 11743532, 16711680),
/*  39 */   BLACK(15, "black", 1908001, MapColor.COLOR_BLACK, 1973019, 0); private static final IntFunction<DyeColor> BY_ID; private static final Int2ObjectOpenHashMap<DyeColor> BY_FIREWORK_COLOR;
/*     */   
/*     */   static  {
/*  42 */     BY_ID = ByIdMap.continuous(DyeColor::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*  43 */     BY_FIREWORK_COLOR = new Int2ObjectOpenHashMap((Map)Arrays.stream(values()).collect(Collectors.toMap(v -> Integer.valueOf(v.fireworkColor), v -> v)));
/*     */     
/*  45 */     CODEC = StringRepresentable.fromEnum(DyeColor::values);
/*  46 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, DyeColor::getId);
/*     */ 
/*     */     
/*  49 */     LEGACY_ID_CODEC = Codec.BYTE.xmap(DyeColor::byId, color -> Byte.valueOf((byte)color.id));
/*     */   }
/*     */   public static final StringRepresentable.EnumCodec<DyeColor> CODEC; public static final StreamCodec<ByteBuf, DyeColor> STREAM_CODEC; @Deprecated
/*     */   public static final Codec<DyeColor> LEGACY_ID_CODEC; private final int id; private final String name;
/*     */   private final MapColor mapColor;
/*     */   private final int textureDiffuseColor;
/*     */   private final int fireworkColor;
/*     */   private final int textColor;
/*     */   
/*     */   DyeColor(int id, String name, int textureDiffuseColor, MapColor mapColor, int fireworkColor, int textColor) {
/*  59 */     this.id = id;
/*  60 */     this.name = name;
/*  61 */     this.mapColor = mapColor;
/*  62 */     this.textColor = ARGB.opaque(textColor);
/*  63 */     this.textureDiffuseColor = ARGB.opaque(textureDiffuseColor);
/*  64 */     this.fireworkColor = fireworkColor;
/*     */   }
/*     */ 
/*     */   
/*  68 */   public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public int getTextureDiffuseColor() { return this.textureDiffuseColor; }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public MapColor getMapColor() { return this.mapColor; }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public int getFireworkColor() { return this.fireworkColor; }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public int getTextColor() { return this.textColor; }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static DyeColor byId(int id) { return (DyeColor)BY_ID.apply(id); }
/*     */ 
/*     */   
/*     */   @Contract("_,!null->!null;_,null->_")
/*     */   public static DyeColor byName(String name, DyeColor def) {
/*  97 */     DyeColor result = (DyeColor)CODEC.byName(name);
/*  98 */     return (result != null) ? result : def;
/*     */   }
/*     */ 
/*     */   
/* 102 */   public static DyeColor byFireworkColor(int color) { return (DyeColor)BY_FIREWORK_COLOR.get(color); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public String toString() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */   
/*     */   public static DyeColor getMixedColor(ServerLevel level, DyeColor dyeColor1, DyeColor dyeColor2) {
/* 116 */     CraftingInput input = makeCraftColorInput(dyeColor1, dyeColor2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     Objects.requireNonNull(DyeItem.class);
/* 122 */     Objects.requireNonNull(DyeItem.class); return (DyeColor)level.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, level).map(recipe -> ((CraftingRecipe)recipe.value()).assemble(input, level.registryAccess())).map(ItemStack::getItem).filter(DyeItem.class::isInstance).map(DyeItem.class::cast)
/* 123 */       .map(DyeItem::getDyeColor)
/* 124 */       .orElseGet(() -> level.random.nextBoolean() ? dyeColor1 : dyeColor2);
/*     */   }
/*     */   
/*     */   private static CraftingInput makeCraftColorInput(DyeColor dyeColor1, DyeColor dyeColor2) {
/* 128 */     return CraftingInput.of(2, 1, List.of(new ItemStack(
/* 129 */             DyeItem.byColor(dyeColor1)), new ItemStack(
/* 130 */             DyeItem.byColor(dyeColor2))));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\DyeColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */