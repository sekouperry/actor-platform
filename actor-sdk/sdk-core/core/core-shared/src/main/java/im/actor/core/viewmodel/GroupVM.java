/*
 * Copyright (C) 2015 Actor LLC. <https://actor.im>
 */

package im.actor.core.viewmodel;

import com.google.j2objc.annotations.ObjectiveCName;
import com.google.j2objc.annotations.Property;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import im.actor.core.entity.Group;
import im.actor.core.entity.GroupMember;
import im.actor.core.entity.GroupType;
import im.actor.core.viewmodel.generics.AvatarValueModel;
import im.actor.core.viewmodel.generics.BooleanValueModel;
import im.actor.core.viewmodel.generics.IntValueModel;
import im.actor.core.viewmodel.generics.StringValueModel;
import im.actor.runtime.annotations.MainThread;
import im.actor.runtime.mvvm.BaseValueModel;
import im.actor.runtime.mvvm.ModelChangedListener;
import im.actor.runtime.mvvm.ValueModel;
import im.actor.runtime.mvvm.ValueModelCreator;

/**
 * Group View Model
 */
public class GroupVM extends BaseValueModel<Group> {

    public static ValueModelCreator<Group, GroupVM> CREATOR = GroupVM::new;

    @Property("nonatomic, readonly")
    private int groupId;
    @NotNull
    @Property("nonatomic, readonly")
    private GroupType groupType;
    @NotNull
    @Property("nonatomic, readonly")
    private StringValueModel name;
    @NotNull
    @Property("nonatomic, readonly")
    private AvatarValueModel avatar;
    @NotNull
    @Property("nonatomic, readonly")
    private BooleanValueModel isMember;
    @NotNull
    @Property("nonatomic, readonly")
    private IntValueModel membersCount;
    @NotNull
    @Property("nonatomic, readonly")
    private BooleanValueModel isCanWriteMessage;

    @NotNull
    @Property("nonatomic, readonly")
    private ValueModel<HashSet<GroupMember>> members;

    @Property("nonatomic, readonly")
    private IntValueModel ownerId;
    @NotNull
    @Property("nonatomic, readonly")
    private ValueModel<Integer> presence;
    @NotNull
    @Property("nonatomic, readonly")
    private StringValueModel theme;
    @NotNull
    @Property("nonatomic, readonly")
    private StringValueModel about;

    @NotNull
    private ArrayList<ModelChangedListener<GroupVM>> listeners = new ArrayList<>();

    /**
     * <p>INTERNAL API</p>
     * Create Group View Model
     *
     * @param rawObj initial value of Group
     */
    public GroupVM(@NotNull Group rawObj) {
        super(rawObj);
        this.groupId = rawObj.getGroupId();
        this.groupType = rawObj.getGroupType();
        this.name = new StringValueModel("group." + groupId + ".title", rawObj.getTitle());
        this.avatar = new AvatarValueModel("group." + groupId + ".avatar", rawObj.getAvatar());
        this.isMember = new BooleanValueModel("group." + groupId + ".isMember", rawObj.isMember());
        this.membersCount = new IntValueModel("group." + groupId + ".membersCount", rawObj.getMembersCount());
        this.isCanWriteMessage = new BooleanValueModel("group." + groupId + ".can_write", rawObj.isCanWrite());

        this.ownerId = new IntValueModel("group." + groupId + ".membersCount", rawObj.getOwnerId());
        this.members = new ValueModel<>("group." + groupId + ".members", new HashSet<>(rawObj.getMembers()));
        this.presence = new ValueModel<>("group." + groupId + ".presence", 0);
        this.theme = new StringValueModel("group." + groupId + ".theme", rawObj.getTopic());
        this.about = new StringValueModel("group." + groupId + ".about", rawObj.getAbout());
    }

    /**
     * Get Group Id
     *
     * @return Group Id
     */
    @ObjectiveCName("getId")
    public int getId() {
        return groupId;
    }

    /**
     * Get Group Type
     *
     * @return Group Type
     */
    @NotNull
    @ObjectiveCName("getGroupType")
    public GroupType getGroupType() {
        return groupType;
    }

    /**
     * Get Name Value Model
     *
     * @return Value Model of String
     */
    @NotNull
    @ObjectiveCName("getNameModel")
    public StringValueModel getName() {
        return name;
    }

    /**
     * Get Avatar Value Model
     *
     * @return Value Model of Avatar
     */
    @NotNull
    @ObjectiveCName("getAvatarModel")
    public AvatarValueModel getAvatar() {
        return avatar;
    }

    /**
     * Get membership Value Model
     *
     * @return Value Model of Boolean
     */
    @NotNull
    @ObjectiveCName("isMemberModel")
    public BooleanValueModel isMember() {
        return isMember;
    }

    /**
     * Get Group members count
     *
     * @return members count
     */
    @NotNull
    @ObjectiveCName("getMembersCountModel")
    public IntValueModel getMembersCount() {
        return membersCount;
    }

    /**
     * Can current user write message to a group
     *
     * @return can write message model
     */
    @NotNull
    @ObjectiveCName("isCanWriteMessageModel")
    public BooleanValueModel getIsCanWriteMessage() {
        return isCanWriteMessage;
    }

    /**
     * Get Group owner user id model
     *
     * @return creator owner id model
     */
    @ObjectiveCName("getCreatorIdModel")
    public IntValueModel getOwnerId() {
        return ownerId;
    }

    /**
     * Get members Value Model
     *
     * @return Value Model of HashSet of GroupMember
     */
    @NotNull
    @ObjectiveCName("getMembersModel")
    public ValueModel<HashSet<GroupMember>> getMembers() {
        return members;
    }

    /**
     * Get Online Value Model
     *
     * @return Value Model of Integer
     */
    @NotNull
    @ObjectiveCName("getPresenceModel")
    public ValueModel<Integer> getPresence() {
        return presence;
    }

    @Override
    protected void updateValues(@NotNull Group rawObj) {
        boolean isChanged = name.change(rawObj.getTitle());
        isChanged |= avatar.change(rawObj.getAvatar());
        isChanged |= membersCount.change(rawObj.getMembersCount());
        isChanged |= isMember.change(rawObj.isMember());
        isChanged |= isCanWriteMessage.change(rawObj.isCanWrite());

        isChanged |= theme.change(rawObj.getTopic());
        isChanged |= about.change(rawObj.getAbout());
        isChanged |= members.change(new HashSet<>(rawObj.getMembers()));
        isChanged |= ownerId.change(rawObj.getOwnerId());

        if (isChanged) {
            notifyChange();
        }
    }

    /**
     * Get About Value Model
     *
     * @return Value Model of String
     */
    @NotNull
    @ObjectiveCName("getAboutModel")
    public StringValueModel getAbout() {
        return about;
    }

    /**
     * Get Theme Value Model
     *
     * @return Value Model of String
     */
    @NotNull
    @ObjectiveCName("getThemeModel")
    public StringValueModel getTheme() {
        return theme;
    }

    /**
     * Subscribe for GroupVM updates
     *
     * @param listener Listener for updates
     */
    @MainThread
    @ObjectiveCName("subscribeWithListener:")
    public void subscribe(@NotNull ModelChangedListener<GroupVM> listener) {
        // im.actor.runtime.Runtime.checkMainThread();
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
        listener.onChanged(this);
    }

    /**
     * Subscribe for GroupVM updates
     *
     * @param listener Listener for updates
     */
    @MainThread
    @ObjectiveCName("subscribeWithListener:withNotify:")
    public void subscribe(@NotNull ModelChangedListener<GroupVM> listener, boolean notify) {
        // im.actor.runtime.Runtime.checkMainThread();
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
        if (notify) {
            listener.onChanged(this);
        }
    }

    /**
     * Unsubscribe from GroupVM updates
     *
     * @param listener Listener for updates
     */
    @MainThread
    @ObjectiveCName("unsubscribeWithListener:")
    public void unsubscribe(@NotNull ModelChangedListener<GroupVM> listener) {
        // im.actor.runtime.Runtime.checkMainThread();
        listeners.remove(listener);
    }

    private void notifyChange() {
        im.actor.runtime.Runtime.postToMainThread(() -> {
            for (ModelChangedListener<GroupVM> l : listeners) {
                l.onChanged(GroupVM.this);
            }
        });
    }

    private boolean isHaveMember(int uid, Collection<GroupMember> members) {
        for (GroupMember m : members) {
            if (m.getUid() == uid) {
                return true;
            }
        }
        return false;
    }
}