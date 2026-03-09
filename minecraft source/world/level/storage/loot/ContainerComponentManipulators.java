/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.BundleContents;
/*    */ import net.minecraft.world.item.component.ChargedProjectiles;
/*    */ import net.minecraft.world.item.component.ItemContainerContents;
/*    */ 
/*    */ public interface ContainerComponentManipulators {
/* 18 */   public static final ContainerComponentManipulator<ItemContainerContents> CONTAINER = new ContainerComponentManipulator<ItemContainerContents>()
/*    */     {
/*    */       public DataComponentType<ItemContainerContents> type() {
/* 21 */         return DataComponents.CONTAINER;
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 26 */       public Stream<ItemStack> getContents(ItemContainerContents component) { return component.stream(); }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 31 */       public ItemContainerContents empty() { return ItemContainerContents.EMPTY; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 36 */       public ItemContainerContents setContents(ItemContainerContents component, Stream<ItemStack> newContents) { return ItemContainerContents.fromItems(newContents.toList()); }
/*    */     };
/*    */ 
/*    */   
/* 40 */   public static final ContainerComponentManipulator<BundleContents> BUNDLE_CONTENTS = new ContainerComponentManipulator<BundleContents>()
/*    */     {
/*    */       public DataComponentType<BundleContents> type() {
/* 43 */         return DataComponents.BUNDLE_CONTENTS;
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 48 */       public BundleContents empty() { return BundleContents.EMPTY; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 53 */       public Stream<ItemStack> getContents(BundleContents component) { return component.itemCopyStream(); }
/*    */ 
/*    */ 
/*    */       
/*    */       public BundleContents setContents(BundleContents component, Stream<ItemStack> newContents) {
/* 58 */         BundleContents.Mutable builder = (new BundleContents.Mutable(component)).clearItems();
/* 59 */         Objects.requireNonNull(builder); newContents.forEach(builder::tryInsert);
/* 60 */         return builder.toImmutable();
/*    */       }
/*    */     };
/*    */   
/* 64 */   public static final ContainerComponentManipulator<ChargedProjectiles> CHARGED_PROJECTILES = new ContainerComponentManipulator<ChargedProjectiles>()
/*    */     {
/*    */       public DataComponentType<ChargedProjectiles> type() {
/* 67 */         return DataComponents.CHARGED_PROJECTILES;
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 72 */       public ChargedProjectiles empty() { return ChargedProjectiles.EMPTY; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 77 */       public Stream<ItemStack> getContents(ChargedProjectiles component) { return component.getItems().stream(); }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 82 */       public ChargedProjectiles setContents(ChargedProjectiles component, Stream<ItemStack> newContents) { return ChargedProjectiles.of(newContents.toList()); }
/*    */     };
/*    */ 
/*    */   
/* 86 */   public static final Map<DataComponentType<?>, ContainerComponentManipulator<?>> ALL_MANIPULATORS = (Map)Stream.of(new ContainerComponentManipulator[] { CONTAINER, BUNDLE_CONTENTS, CHARGED_PROJECTILES
/*    */ 
/*    */ 
/*    */       
/* 90 */       }).collect(Collectors.toMap(ContainerComponentManipulator::type, e -> e));
/*    */   
/* 92 */   public static final Codec<ContainerComponentManipulator<?>> CODEC = BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec().comapFlatMap(type -> {
/*    */         
/* 94 */         ContainerComponentManipulator<?> manipulator = (ContainerComponentManipulator)ALL_MANIPULATORS.get(type);
/* 95 */         return (manipulator != null) ? DataResult.success(manipulator) : DataResult.error(());
/*    */       }ContainerComponentManipulator::type);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\ContainerComponentManipulators.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */