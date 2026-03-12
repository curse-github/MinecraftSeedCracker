/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ public class DataPackConfig {
/* 11 */   public static final DataPackConfig DEFAULT = new DataPackConfig(ImmutableList.of("vanilla"), ImmutableList.of());
/*    */   
/* 13 */   public static final Codec<DataPackConfig> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING
/* 14 */         .listOf().fieldOf("Enabled").forGetter(()), Codec.STRING
/* 15 */         .listOf().fieldOf("Disabled").forGetter(()))
/* 16 */       .apply(i, DataPackConfig::new));
/*    */   
/*    */   private final List<String> enabled;
/*    */   private final List<String> disabled;
/*    */   
/*    */   public DataPackConfig(List<String> enabled, List<String> disabled) {
/* 22 */     this.enabled = ImmutableList.copyOf(enabled);
/* 23 */     this.disabled = ImmutableList.copyOf(disabled);
/*    */   }
/*    */ 
/*    */   
/* 27 */   public List<String> getEnabled() { return this.enabled; }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public List<String> getDisabled() { return this.disabled; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\DataPackConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */