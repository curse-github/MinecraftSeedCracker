/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.TooltipFlag;
/*    */ import net.minecraft.world.item.component.MapPostProcessing;
/*    */ 
/*    */ public final class MapId extends Record implements TooltipProvider {
/*    */   private final int id;
/*    */   
/* 18 */   public MapId(int id) { this.id = id; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapId;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapId; } public int id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapId;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapId; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapId;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapId;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 19 */   public static final Codec<MapId> CODEC = Codec.INT.xmap(MapId::new, MapId::id);
/* 20 */   public static final StreamCodec<ByteBuf, MapId> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(MapId::new, MapId::id);
/*    */   
/* 22 */   private static final Component LOCKED_TEXT = Component.translatable("filled_map.locked").withStyle(ChatFormatting.GRAY);
/*    */ 
/*    */   
/* 25 */   public String key() { return "map_" + this.id; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 30 */     MapItemSavedData data = context.mapData(this);
/* 31 */     if (data == null) {
/* 32 */       consumer.accept(Component.translatable("filled_map.unknown").withStyle(ChatFormatting.GRAY));
/*    */       
/*    */       return;
/*    */     } 
/* 36 */     MapPostProcessing postProcessing = (MapPostProcessing)components.get(DataComponents.MAP_POST_PROCESSING);
/*    */ 
/*    */     
/* 39 */     if (components.get(DataComponents.CUSTOM_NAME) == null && postProcessing == null) {
/* 40 */       consumer.accept(Component.translatable("filled_map.id", new Object[] { Integer.valueOf(this.id) }).withStyle(ChatFormatting.GRAY));
/*    */     }
/*    */     
/* 43 */     if (data.locked || postProcessing == MapPostProcessing.LOCK) {
/* 44 */       consumer.accept(LOCKED_TEXT);
/*    */     }
/*    */     
/* 47 */     if (flag.isAdvanced()) {
/* 48 */       int scaleToAdd = (postProcessing == MapPostProcessing.SCALE) ? 1 : 0;
/* 49 */       int scale = Math.min(data.scale + scaleToAdd, 4);
/* 50 */       consumer.accept(Component.translatable("filled_map.scale", new Object[] { Integer.valueOf(1 << scale) }).withStyle(ChatFormatting.GRAY));
/* 51 */       consumer.accept(Component.translatable("filled_map.level", new Object[] { Integer.valueOf(scale), Integer.valueOf(4) }).withStyle(ChatFormatting.GRAY));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\saveddata\maps\MapId.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */