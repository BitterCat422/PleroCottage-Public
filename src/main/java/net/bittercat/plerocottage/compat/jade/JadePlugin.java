package net.bittercat.plerocottage.compat.jade;

import net.bittercat.plerocottage.blocks.AbstractFruitLeavesBlock;
import net.bittercat.plerocottage.blocks.BerryBushBlock;
import net.bittercat.plerocottage.blocks.VineArborBlock;
import net.bittercat.plerocottage.blocks.VineStakeBlock;
import net.bittercat.plerocottage.entities.PoultryEntity;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(TrimmedBushComponentProvider.INSTANCE, BerryBushBlock.class);
        registration.registerBlockComponent(FruitLeavesComponentProvider.INSTANCE, AbstractFruitLeavesBlock.class);
        registration.registerBlockComponent(VineStakeComponentProvider.INSTANCE, VineStakeBlock.class);
        registration.registerBlockComponent(VineArborComponentProvider.INSTANCE, VineArborBlock.class);
        registration.registerEntityComponent(PoultryEggComponentProvider.INSTANCE, PoultryEntity.class);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(PoultryEggComponentProvider.INSTANCE, PoultryEntity.class);
    }
}
