/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.entity.BannerBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ public final class MapBanner extends Record {
/*    */   private final BlockPos pos;
/*    */   private final DyeColor color;
/*    */   private final Optional<Component> name;
/*    */   
/* 17 */   public MapBanner(BlockPos pos, DyeColor color, Optional<Component> name) { this.pos = pos; this.color = color; this.name = name; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapBanner;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapBanner; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapBanner;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapBanner; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapBanner;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapBanner;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public BlockPos pos() { return this.pos; } public DyeColor color() { return this.color; } public Optional<Component> name() { return this.name; }
/* 18 */   public static final Codec<MapBanner> CODEC = RecordCodecBuilder.create(i -> i.group(BlockPos.CODEC
/* 19 */         .fieldOf("pos").forGetter(MapBanner::pos), DyeColor.CODEC
/* 20 */         .lenientOptionalFieldOf("color", DyeColor.WHITE).forGetter(MapBanner::color), ComponentSerialization.CODEC
/* 21 */         .lenientOptionalFieldOf("name").forGetter(MapBanner::name))
/* 22 */       .apply(i, MapBanner::new));
/*    */   
/*    */   public static MapBanner fromWorld(BlockGetter level, BlockPos pos) {
/* 25 */     BlockEntity entity = level.getBlockEntity(pos);
/* 26 */     if (entity instanceof BannerBlockEntity) { BannerBlockEntity banner = (BannerBlockEntity)entity;
/* 27 */       DyeColor color = banner.getBaseColor();
/* 28 */       Optional<Component> name = Optional.ofNullable(banner.getCustomName());
/* 29 */       return new MapBanner(pos, color, name); }
/*    */     
/* 31 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder<MapDecorationType> getDecoration() {
/* 36 */     switch (this.color) { default: throw new MatchException(null, null);case WHITE: case ORANGE: case MAGENTA: case LIGHT_BLUE: case YELLOW: case LIME: case PINK: case GRAY: case LIGHT_GRAY: case CYAN: case PURPLE: case BLUE: case BROWN: case GREEN: case RED: case BLACK: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 52 */       MapDecorationTypes.BLACK_BANNER;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public String getId() { return "banner-" + this.pos.getX() + "," + this.pos.getY() + "," + this.pos.getZ(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\saveddata\maps\MapBanner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */