// src/main/webapp/app/layouts/navbar/navbar-item.model.ts
type NavbarItem = {
  name: string;
  route: string;
  translationKey: string;
  icon?: string;
  children?: NavbarItem[];
  authority?: string | string[];
};

export default NavbarItem;
