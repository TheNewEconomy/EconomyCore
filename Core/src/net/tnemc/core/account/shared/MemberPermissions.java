package net.tnemc.core.account.shared;

/**
 * Represents the {@link Permission permissions} for {@link Member members}.
 *
 *
 * @see Member
 * @see Permission
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public enum MemberPermissions implements Permission {

  /**
   * Can the member check the balance?
   */
  BALANCE {

    /**
     * The identifier of the permission.
     *
     * @return The identifier of the permission.
     */
    @Override
    public String identifier() {
      return "BALANCE";
    }

    /**
     * The default value of the permission.
     *
     * @return The default value of the permission.
     */
    @Override
    public boolean defaultValue() {
      return true;
    }
  },

  /**
   * Can the member withdraw from the account?
   */
  WITHDRAW {

    /**
     * The identifier of the permission.
     *
     * @return The identifier of the permission.
     */
    @Override
    public String identifier() {
      return "WITHDRAW";
    }

    /**
     * The default value of the permission.
     *
     * @return The default value of the permission.
     */
    @Override
    public boolean defaultValue() {
      return false;
    }
  },

  /**
   * Can the member deposit to the account?
   */
  DEPOSIT {

    /**
     * The identifier of the permission.
     *
     * @return The identifier of the permission.
     */
    @Override
    public String identifier() {
      return "DEPOSIT";
    }

    /**
     * The default value of the permission.
     *
     * @return The default value of the permission.
     */
    @Override
    public boolean defaultValue() {
      return false;
    }
  },

  /**
   * Can the member add other members?
   */
  ADD_MEMBER {

    /**
     * The identifier of the permission.
     *
     * @return The identifier of the permission.
     */
    @Override
    public String identifier() {
      return "ADD_MEMBER";
    }

    /**
     * The default value of the permission.
     *
     * @return The default value of the permission.
     */
    @Override
    public boolean defaultValue() {
      return false;
    }
  },

  /**
   * Can the member modify the permissions of other members?
   */
  MODIFY_MEMBER {

    /**
     * The identifier of the permission.
     *
     * @return The identifier of the permission.
     */
    @Override
    public String identifier() {
      return "MODIFY_MEMBER";
    }

    /**
     * The default value of the permission.
     *
     * @return The default value of the permission.
     */
    @Override
    public boolean defaultValue() {
      return false;
    }
  },

  /**
   * Can the member remove other members?
   */
  REMOVE_MEMBER {

    /**
     * The identifier of the permission.
     *
     * @return The identifier of the permission.
     */
    @Override
    public String identifier() {
      return "REMOVE_MEMBER";
    }

    /**
     * The default value of the permission.
     *
     * @return The default value of the permission.
     */
    @Override
    public boolean defaultValue() {
      return false;
    }
  }
}