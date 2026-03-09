/*    */ package net.minecraft.gametest.framework;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class FunctionGameTestInstance extends GameTestInstance {
/* 14 */   public static final MapCodec<FunctionGameTestInstance> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 15 */         ResourceKey.codec(Registries.TEST_FUNCTION).fieldOf("function").forGetter(FunctionGameTestInstance::function), TestData.CODEC
/* 16 */         .forGetter(GameTestInstance::info))
/* 17 */       .apply(i, FunctionGameTestInstance::new));
/*    */   
/*    */   private final ResourceKey<Consumer<GameTestHelper>> function;
/*    */ 
/*    */   
/*    */   public FunctionGameTestInstance(ResourceKey<Consumer<GameTestHelper>> function, TestData<Holder<TestEnvironmentDefinition>> info) {
/* 23 */     super(info);
/* 24 */     this.function = function;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(GameTestHelper helper) {
/* 29 */     ((Consumer)helper.getLevel().registryAccess().get(this.function)
/* 30 */       .map(Holder.Reference::value)
/* 31 */       .orElseThrow(() -> new IllegalStateException("Trying to access missing test function: " + String.valueOf(this.function.identifier()))))
/* 32 */       .accept(helper);
/*    */   }
/*    */ 
/*    */   
/* 36 */   private ResourceKey<Consumer<GameTestHelper>> function() { return this.function; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public MapCodec<FunctionGameTestInstance> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   protected MutableComponent typeDescription() { return Component.translatable("test_instance.type.function"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public Component describe() { return describeType()
/* 52 */       .append(descriptionRow("test_instance.description.function", this.function.identifier().toString()))
/* 53 */       .append(describeInfo()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\FunctionGameTestInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */