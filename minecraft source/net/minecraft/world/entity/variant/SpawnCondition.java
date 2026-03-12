/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SpawnCondition
/*    */   extends PriorityProvider.SelectorCondition<SpawnContext>
/*    */ {
/* 12 */   public static final Codec<SpawnCondition> CODEC = BuiltInRegistries.SPAWN_CONDITION_TYPE.byNameCodec().dispatch(SpawnCondition::codec, c -> c);
/*    */   
/*    */   MapCodec<? extends SpawnCondition> codec();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\variant\SpawnCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */