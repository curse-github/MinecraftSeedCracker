/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.component.TypedEntityData;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ 
/*    */ 
/*    */ public interface Spawner
/*    */ {
/*    */   static void appendHoverText(TypedEntityData<BlockEntityType<?>> data, Consumer<Component> consumer, String nextSpawnDataTagKey) {
/* 19 */     Component displayName = getSpawnEntityDisplayName(data, nextSpawnDataTagKey);
/*    */     
/* 21 */     if (displayName != null) {
/* 22 */       consumer.accept(displayName);
/*    */     } else {
/* 24 */       consumer.accept(CommonComponents.EMPTY);
/* 25 */       consumer.accept(Component.translatable("block.minecraft.spawner.desc1").withStyle(ChatFormatting.GRAY));
/* 26 */       consumer.accept(CommonComponents.space().append(Component.translatable("block.minecraft.spawner.desc2").withStyle(ChatFormatting.BLUE)));
/*    */     } 
/*    */   }
/*    */   
/*    */   static Component getSpawnEntityDisplayName(TypedEntityData<BlockEntityType<?>> data, String nextSpawnDataTagKey) {
/* 31 */     if (data == null) {
/* 32 */       return null;
/*    */     }
/* 34 */     return (Component)data.getUnsafe().getCompound(nextSpawnDataTagKey)
/* 35 */       .flatMap(nextSpawnData -> nextSpawnData.getCompound("entity"))
/* 36 */       .flatMap(entityTag -> entityTag.read("id", EntityType.CODEC))
/* 37 */       .map(entityType -> Component.translatable(entityType.getDescriptionId()).withStyle(ChatFormatting.GRAY))
/* 38 */       .orElse(null);
/*    */   }
/*    */   
/*    */   void setEntityId(EntityType<?> paramEntityType, RandomSource paramRandomSource);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\Spawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */