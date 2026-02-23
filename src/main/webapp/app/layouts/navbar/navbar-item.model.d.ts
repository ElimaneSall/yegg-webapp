// src/main/webapp/app/layouts/navbar/navbar-item.model.ts
import { IconProp } from '@fortawesome/fontawesome-svg-core';

type NavbarItem = {
  name: string;
  route: string;
  translationKey: string;
  icon: IconProp;
  children?: NavbarItem[];
  authority?: string | string[];
};

export default NavbarItem;
